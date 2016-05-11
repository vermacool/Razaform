package com.sked.razaform.net;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.sked.razaform.common.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.ContentType;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;

public class MultipartRequest extends Request<String> {
    private static final String FILE_PART_NAME = "file";
    private final Response.Listener<String> mListener;
    private final Map<String, File> mFilePart;
    private final Map<String, String> mStringPart;
    MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    HttpEntity httpentity;

    public MultipartRequest(String url, Response.ErrorListener errorListener,
                            Response.Listener<String> listener, Map<String, File> file,
                            Map<String, String> mStringPart) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mFilePart = file;
        this.mStringPart = mStringPart;
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        buildMultipartEntity();
    }

    public void addStringBody(String param, String value) {
        mStringPart.put(param, value);
    }

    private void buildMultipartEntity() {
        for (Map.Entry<String, File> entry : mFilePart.entrySet()) {
            // entity.addPart(entry.getKey(), new FileBody(entry.getValue(), ContentType.create("image/jpeg"), entry.getKey()));
            try {
                entity.addBinaryBody(entry.getKey(), Utils.toByteArray(new FileInputStream(entry.getValue())), ContentType.create("image/jpeg"), entry.getKey() + ".JPG");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for (Map.Entry<String, String> entry : mStringPart.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                entity.addTextBody(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public String getBodyContentType() {
        return httpentity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpentity = entity.build();
            httpentity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        Log.d("Network", new String(response.data));
        return Response.success(new String(response.data), getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}