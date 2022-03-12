package com.it.lpw.game.paytm;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.it.lpw.game.BuildConfig;
import com.it.lpw.game.paytm.beans.ChecksumResponse;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaytmUtil {

    private Context context;
    private int ActivityRequestCode = 2;

    public PaytmUtil(Context context) {
        this.context = context;
    }

    public String initiateTransaction(int amount, String orderId, String custId) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait...");
        progressDialog.show();

        try {

            JSONObject paytmParams = new JSONObject();

            JSONObject body = new JSONObject();
            body.put("requestType", "Payment");
            body.put("mid", BuildConfig.PAYTM_MID);
            body.put("PAYTM_MERCHANT_WEBSITE", "WEBSTAGING");
            body.put("orderId", orderId);
            body.put("callbackUrl", BuildConfig.VERIFY_URL + "?ORDER_ID=" + orderId);

            JSONObject txnAmount = new JSONObject();
            txnAmount.put("value", amount);
            txnAmount.put("currency", "INR");

            JSONObject userInfo = new JSONObject();
            userInfo.put("custId", custId);
            body.put("txnAmount", txnAmount);
            body.put("userInfo", userInfo);

            String checksum = PaytmChecksum.generateSignature(body.toString(), BuildConfig.MERCHANT_KEY);

            JSONObject head = new JSONObject();
            head.put("signature", checksum);

            paytmParams.put("body", body);
            paytmParams.put("head", head);

            String post_data = paytmParams.toString();

            /* for Staging */
            URL url = new URL(BuildConfig.URL + "/theia/api/v1/initiateTransaction?mid=" + BuildConfig.PAYTM_MID + "&orderId=" + orderId + "");

            /* for Production */
//          URL url = new URL("https://securegw.paytm.in/theia/api/v1/initiateTransaction?mid=YOUR_MID_HERE&orderId=ORDERID_98765");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                Log.e("Response--", responseData);

                progressDialog.dismiss();

                ChecksumResponse response = new Gson().fromJson(responseData, ChecksumResponse.class);

                return response.body.txnToken;
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return "";
    }

    public void initiateTransfer(int amount, String orderId) {

        try {

            Date date = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = df.format(date);

            JSONObject paytmParams = new JSONObject();
            paytmParams.put("subwalletGuid", "28054249-XXXX-XXXX-af8f-fa163e429e83");
            paytmParams.put("orderId", orderId);
            paytmParams.put("beneficiaryAccount", "1913954787");
            paytmParams.put("beneficiaryIFSC", "KKBK0000526");
            paytmParams.put("amount", amount);
            paytmParams.put("purpose", "SALARY_DISBURSEMENT");
            paytmParams.put("date", formattedDate);

            String post_data = paytmParams.toString();

            String checksum = PaytmChecksum.generateSignature(post_data, BuildConfig.MERCHANT_KEY);

            String x_mid = BuildConfig.PAYTM_MID;
            String x_checksum = checksum;

            URL url = new URL(BuildConfig.TRANSFER);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("x-mid", x_mid);
            connection.setRequestProperty("x-checksum", x_checksum);
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response: " + responseData);
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
