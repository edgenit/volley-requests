package com.android.volley;

import android.support.annotation.NonNull;

import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Class to parse a list of all items in a collection from a NetworkResponse
 * depending on a key.
 *
 * @author fpredassi
 *
 * @param <T> The type of the request being decorated.
 */
public class JSONArrayRequestDecorator<T> extends RequestDecorator<T> {
	private final String elementsKey;

	public JSONArrayRequestDecorator(@NonNull final Request<T> request, @NonNull final String elementsKey) {
		super(request);
		this.elementsKey = elementsKey;
	}

	@Override
	protected Response<T> parseNetworkResponse(final NetworkResponse response) {
		try {
			final String headersCharset = HttpHeaderParser.parseCharset(response.headers);
			final String json = new String(response.data, headersCharset);
			final JSONObject object = new JSONObject(json);
			final JSONArray array = object.getJSONArray(elementsKey);
			final byte[] data = array.toString().getBytes(headersCharset);
			final NetworkResponse responseJsonArray = new NetworkResponse(data, response.headers);
			return super.parseNetworkResponse(responseJsonArray);
		} catch (final UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (final JSONException e) {
			return Response.error(new ParseError(e));
		}
	}

	@NonNull
	public String getElementsKey() {
		return elementsKey;
	}

	@Override
	public String toString() {
		return "JSONArrayRequestDecorator{"
				+ "elementsKey='" + elementsKey + '\''
				+ "} " + super.toString();
	}
}
