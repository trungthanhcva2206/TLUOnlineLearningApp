package com.nhom1.tlulearningonline;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public abstract class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final Response.Listener<NetworkResponse> mListener;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final String fileKey;
    private final String fileName;
    private final byte[] fileData;
    private final String boundary;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener,
                                  Map<String, String> params,
                                  Map<String, String> headers,
                                  String fileKey,
                                  String fileName,
                                  byte[] fileData) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.params = params;
        this.headers = headers;
        this.fileKey = fileKey;
        this.fileName = fileName;
        this.fileData = fileData;
        this.boundary = "apiclient-" + System.currentTimeMillis(); // unique boundary
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            // Ghi phần form-data (các tham số text)
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    bos.write(("--" + boundary + "\r\n").getBytes());
                    bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes());
                    bos.write((entry.getValue() + "\r\n").getBytes());
                }
            }

            // Ghi phần file đính kèm
            if (fileData != null && fileName != null) {
                bos.write(("--" + boundary + "\r\n").getBytes());
                bos.write(("Content-Disposition: form-data; name=\"" + fileKey + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
                bos.write(("Content-Type: application/octet-stream\r\n\r\n").getBytes());
                bos.write(fileData);
                bos.write("\r\n".getBytes());
            }

            // Kết thúc multipart
            bos.write(("--" + boundary + "--\r\n").getBytes());

            return bos.toByteArray();

        } catch (IOException e) {
            throw new AuthFailureError("IOException writing multipart body");
        }
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }
}
