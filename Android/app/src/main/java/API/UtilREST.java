package API;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UtilREST {

    public interface OnResponseListener {
        void onSuccess(Response r);
        void onError(Response r);
    }

    public enum QueryType {
        GET, POST, PUT, DELETE
    }

    public static class Response {
        public int responseCode = -1;
        public String content = null;
        public String exception = null;
    }

    private static class Request {
        public QueryType type;
        public String url;
        public String data;
        public String token;
        public OnResponseListener callback;
    }

    public static void runQuery(QueryType type, String url, OnResponseListener listener) {
        runQuery(type, url, null, null, listener);
    }

    public static void runQuery(QueryType type, String url, String data, OnResponseListener listener) {
        runQuery(type, url, data, null, listener);
    }

    public static void runQuery(QueryType type, String url, String data, String token, OnResponseListener listener) {
        Request request = new Request();
        request.type = type;
        request.url = url;
        request.data = data;
        request.token = token;
        request.callback = listener;

        new DownloadTask().execute(request);
    }

    private static class DownloadTask extends AsyncTask<Request, Void, Response> {

        private Request request;

        @Override
        protected Response doInBackground(Request... params) {
            HttpURLConnection http = null;
            Response response = new Response();

            try {
                request = params[0];

                URL url = new URL(request.url);
                http = (HttpURLConnection) url.openConnection();

                http.setRequestProperty("Accept", "application/json");
                http.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                if (request.token != null && !request.token.isEmpty()) {
                    http.setRequestProperty("Authorization", "Bearer " + request.token);
                }

                switch (request.type) {
                    case GET:
                        http.setRequestMethod("GET");
                        break;
                    case POST:
                        http.setRequestMethod("POST");
                        break;
                    case PUT:
                        http.setRequestMethod("PUT");
                        break;
                    case DELETE:
                        http.setRequestMethod("DELETE");
                        break;
                }

                if (request.type == QueryType.POST || request.type == QueryType.PUT) {
                    http.setDoOutput(true);

                    PrintWriter writer = new PrintWriter(http.getOutputStream());
                    writer.print(request.data);
                    writer.flush();
                    writer.close();
                }

                response.responseCode = http.getResponseCode();

                if (response.responseCode >= 200 && response.responseCode < 300) {
                    response.content = readStream(http.getInputStream());
                } else {
                    response.content = readStream(http.getErrorStream());
                }

                Log.d("UtilREST", "URL: " + request.url);
                Log.d("UtilREST", "CODE: " + response.responseCode);
                Log.d("UtilREST", "CONTENT: " + response.content);

            } catch (Exception e) {
                response.responseCode = 500;
                response.exception = e.getMessage();
                Log.e("UtilREST", "ERROR", e);

            } finally {
                if (http != null) {
                    http.disconnect();
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            if (response.responseCode >= 200 && response.responseCode < 300) {
                request.callback.onSuccess(response);
            } else {
                if (response.exception == null) {
                    response.exception = response.content;
                }
                request.callback.onError(response);
            }
        }

        private String readStream(InputStream inputStream) {
            if (inputStream == null) return "";

            StringBuilder builder = new StringBuilder();

            try {
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return builder.toString();
        }
    }
}