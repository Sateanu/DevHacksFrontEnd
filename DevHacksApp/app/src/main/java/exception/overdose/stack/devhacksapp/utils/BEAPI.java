package exception.overdose.stack.devhacksapp.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exception.overdose.stack.devhacksapp.database.RestaurantDataSource;
import exception.overdose.stack.devhacksapp.managers.OrdersManager;
import exception.overdose.stack.devhacksapp.managers.RestaurantsManager;
import exception.overdose.stack.devhacksapp.models.POJO.Orders;
import exception.overdose.stack.devhacksapp.models.POJO.Restaurant;
import exception.overdose.stack.devhacksapp.models.POJO.SubOrder;

/**
 * Created by alexbuicescu on 21.11.2015.
 */
public class BEAPI {

    public static class GetRestaurantsAsync extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            StringBuilder result = new StringBuilder();
            try {
            URL url = new URL("http://192.168.2.172:8008/api/RestaurantsApi/GetRestaurants");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            Gson gson = new Gson();
//                Restaurant[] response = gson.fromJson(result.toString(), Restaurant[].class);
                Restaurant[] response = gson.fromJson(result.toString(), Restaurant[].class);
                Log.i("restaurant:", result.toString());
                if(response.length > 0)
                {
                    RestaurantsManager.getRestaurantsManager().setRestaurants(new ArrayList<>(Arrays.asList(response)));
//                    for(Restaurant restaurant : response)
//                    {
//                        Log.i("restaurant:", restaurant.getName() + " " + restaurant.getLatitude() + " " + restaurant.getSpecific());
//                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
//            return response;
            return null;
        }

    }

    public static class GetOrdersAsync extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            StringBuilder result = new StringBuilder();
            try {
            URL url = new URL("http://192.168.2.172:8008/api/RestaurantsApi/GetRestaurants");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            Gson gson = new Gson();
//                Restaurant[] response = gson.fromJson(result.toString(), Restaurant[].class);
                Orders[] response = gson.fromJson(result.toString(), Orders[].class);
                Log.i("orders:", result.toString());
                if(response.length > 0)
                {
                    OrdersManager.getOrdersManager().setOrderses(new ArrayList<>(Arrays.asList(response)));
//                    for(Restaurant restaurant : response)
//                    {
//                        Log.i("restaurant:", restaurant.getName() + " " + restaurant.getLatitude() + " " + restaurant.getSpecific());
//                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
//            return response;
            return null;
        }

    }

    public static class SendOrderAsync extends AsyncTask<Orders, Void, Void>
    {
        private ArrayList<SubOrder> subOrders;
        private long orderId;

        public SendOrderAsync(ArrayList<SubOrder> subOrders)
        {
            this.subOrders = subOrders;
        }

        @Override
        protected Void doInBackground(Orders... params) {
            if(params.length == 0)
            {
                return null;
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.2.172:8008/api/RestaurantsApi/GetRestaurants");

            // Request parameters and other properties.
            List<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("order", new Gson().toJson(params[0])));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();

                StringBuilder result = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(instream));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();

                Gson gson = new Gson();
//                Restaurant[] response = gson.fromJson(result.toString(), Restaurant[].class);
                Orders responseFromBE = gson.fromJson(result.toString(), Orders.class);
                if(responseFromBE != null) {
                    OrdersManager.getOrdersManager().addOrder(responseFromBE);
                    orderId = responseFromBE.getId();
                }

                try {
                    // do something useful
                } finally {
                    instream.close();
                }
            }
            else {
                throw new Exception();
            }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            return response;
            return null;
        }

        @Override
        protected void onPostExecute(Void param){
            new SendSubOrdersAsync(orderId, subOrders).execute();
        }

    }

    public static class SendSubOrdersAsync extends AsyncTask<Void, Void, Void>
    {
        private ArrayList<SubOrder> subOrders;
        private long orderId;

        public SendSubOrdersAsync(long orderId, ArrayList<SubOrder> subOrders)
        {
            this.subOrders = subOrders;
            this.orderId = orderId;
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.2.172:8008/api/RestaurantsApi/GetRestaurants");

            // Request parameters and other properties.
            List<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("suborders", new Gson().toJson(subOrders)));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();

                StringBuilder result = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(instream));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();

                OrdersManager.getOrdersManager().insertSubOrders(orderId, subOrders);

                Gson gson = new Gson();
//                Restaurant[] response = gson.fromJson(result.toString(), Restaurant[].class);
                Orders responseFromBE = gson.fromJson(result.toString(), Orders.class);
                if(responseFromBE != null) {
                    OrdersManager.getOrdersManager().getOrderses().add(responseFromBE);
                }

                try {
                    // do something useful
                } finally {
                    instream.close();
                }
            }
            else {
                throw new Exception();
            }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            return response;
            return null;
        }

    }

    public static class SendEmailAsync extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            if(params.length == 0)
            {
                return null;
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.2.172:8008/api/UsersApi/LoginUser");

            // Request parameters and other properties.
            List<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("email", params[0]));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
//                httppost.setHeader("Content-Type", "application/json");
//                httppost.setEntity(new StringEntity(params[0]));

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();

                StringBuilder result = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(instream));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();

                Gson gson = new Gson();
//                Restaurant[] response = gson.fromJson(result.toString(), Restaurant[].class);
                long responseFromBE = gson.fromJson(result.toString(), long.class);
                Log.i("login", result.toString() + " result " + responseFromBE);

                RestaurantsManager.getRestaurantsManager().setMyId(responseFromBE);
//                if(responseFromBE != null) {
//                    OrdersManager.getOrdersManager().getOrderses().add(responseFromBE);
//                }

                try {
                    // do something useful
                } finally {
                    instream.close();
                }
            }
            else {
                throw new Exception();
            }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            return response;
            return null;
        }

    }
}
