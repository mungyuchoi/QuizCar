package com.moon.quizcar

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingManager(context: Context) {
    private val TAG = "BillingManager"

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            Log.d(TAG, "결제 성공, 아래 구매한 상품나열됨")
            for (pur in purchases) {
                handlePurchase(pur)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(TAG, "사용자에 의해 결제 취소")
        } else {
            Log.d(TAG, "결제가 취소되었습니다. 종료코드 : ${billingResult.responseCode}")
        }
    }
    private val skuDetails = ArrayList<SkuDetails>()

    private val consumeListener = ConsumeResponseListener { billingResult, purchaseToken ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            Log.d(TAG, "상품을 성공적으로 소모:$purchaseToken")
            Toast.makeText(context, "골드가 충전되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "상품 소모에 실패 오류코드:${billingResult.responseCode} 상품코드:$purchaseToken")
        }
    }
    private var billingClient =
        BillingClient.newBuilder(context).setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()

    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Google BillingService connected")
                    updateSkuDetailList()

                    val resultItem = billingClient.queryPurchases(BillingClient.SkuType.INAPP)

                    val listItem = resultItem.purchasesList
                    for (i in 0 until listItem!!.size) {
                        val purchase = listItem[i]
                        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) {
                            // 카드사 승인중인 결제 || 보류중
                        } else {
                            // 결제 승인 완료된 경우
                            handlePurchase(listItem[i])
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "Google BillingService Disconnected")
            }

        })
    }

    fun updateSkuDetailList() {
        val skuList = ArrayList<String>()
        skuList.add("1000")
        skuList.add("5000")
        skuList.add("10000")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
                    if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                        Log.d(TAG, "상품정보 가지고 오던 중 에러")
                        return@querySkuDetailsAsync
                    }
                    if (skuDetailsList == null) {
                        Log.d(TAG, "상품 정보가 존재하지 않습니다.")
                        return@querySkuDetailsAsync
                    }
                    Log.d(TAG, "응답받은 데이터 숫자: ${skuDetailsList.size}")

                    for (i in 0 until skuDetailsList.size) {
                        val skuDetail = skuDetailsList[i]
                        Log.d(
                            TAG,
                            "skuDetails:${skuDetail.sku}, title:${skuDetail.title}, price:${skuDetail.price}"
                        )
                    }
                    skuDetails.clear()
                    skuDetails.addAll(skuDetailsList)
                }
            }
        }
    }

    fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            //인앱소비
            val consumeParams =
                ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
            billingClient.consumeAsync(consumeParams, consumeListener)
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            // 해당 아이템에 대해 소모되지 않은 결제가 있을때
        }
    }

    fun purchase(item: String, activity: Activity) {
        Log.i("MQ!", "purchase item:$item")
        if (skuDetails.size > 0) {
            var skuDetail: SkuDetails? = null
            for (i in 0 until skuDetails.size) {
                if (skuDetails[i].sku == item) {
                    skuDetail = skuDetails[i]
                    break
                }
            }

            val flowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetail!!).build()
            billingClient.launchBillingFlow(activity, flowParams)
        }
    }


}