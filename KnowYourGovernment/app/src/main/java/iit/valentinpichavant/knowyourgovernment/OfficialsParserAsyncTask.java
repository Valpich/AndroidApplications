package iit.valentinpichavant.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by valentinpichavant on 2/28/17.
 */

public class OfficialsParserAsyncTask extends AsyncTask<String, Integer, String> {
    private static final String OFFICIAL_SEARCH_HTTP = "https://www.googleapis.com/civicinfo/v2/representatives?key=";
    private static final String OFFICIAL_SEARCH_API = "AIzaSyA1nZNG9ejpHLT10jxVFBD4jhyXrnq7Lcs";
    private static final String OFFICIAL_SEARCH_PARAM = "&address=";
    private static final String OFFICIAL_SEARCH_PARAM_DEFAULT = "20500";
    private static final String OFFICIAL_SEARCH_QUERY = OFFICIAL_SEARCH_HTTP + OFFICIAL_SEARCH_API + OFFICIAL_SEARCH_PARAM;
    private static final String TAG = "AsyncOfficialsLoader";
    private MainActivity mainActivity;
    private String zipcode;

    public OfficialsParserAsyncTask(MainActivity ma, String zipcode) {
        this.mainActivity = ma;
        this.zipcode = zipcode;
    }

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onPostExecute(String s) {
        mainActivity.setOfficesList(parseJSON(s));
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString;
        if (zipcode != null) {
            urlString = OFFICIAL_SEARCH_QUERY + zipcode;
        } else {
            urlString = OFFICIAL_SEARCH_QUERY + OFFICIAL_SEARCH_PARAM_DEFAULT;
        }
        Uri dataUri = Uri.parse(urlString);
        String urlToUse = dataUri.toString().replaceAll(" ", "%20");
        Log.d(TAG, "doInBackground: " + urlToUse);
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + sb.toString());
        return sb.toString();
    }


    private List<Office> parseJSON(String s) {
        List<Office> officeList = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONObject jObjNormalizedInput = jObjMain.getJSONObject("normalizedInput");
            this.mainActivity.setLastLocation(jObjNormalizedInput.getString("city") + ", " + jObjNormalizedInput.getString("state") + " " + jObjNormalizedInput.getString("zip"));
            this.mainActivity.setAddress(jObjNormalizedInput.getString("city") + ", " + jObjNormalizedInput.getString("state") + " " + jObjNormalizedInput.getString("zip"));
            JSONArray jArrayOffices = jObjMain.getJSONArray("offices");
            JSONArray jArrayOfficials = jObjMain.getJSONArray("officials");
            List<Official> officialsList = new ArrayList<>();
            parseOfficials(jArrayOfficials, officialsList);
            parseOffices(jArrayOffices, officialsList, officeList);
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
        }
        return officeList;
    }

    private void parseOffices(JSONArray jArrayOffices, List<Official> officialsList, List<Office> officeList) throws JSONException {
        for (int i = 0; i < jArrayOffices.length(); i++) {
            JSONObject jOffice = (JSONObject) jArrayOffices.get(i);
            String name = jOffice.getString("name");
            JSONArray jArrayOfficialsIndices = jOffice.getJSONArray("officialIndices");
            for (int k = 0; k < jArrayOfficialsIndices.length(); k++) {
                int officialIndices = Integer.parseInt(jArrayOfficialsIndices.get(k).toString());
                Official official = officialsList.get(officialIndices);
                if (official.getOfficialIndices() == officialIndices) {
                    officeList.add(new Office(name, official));
                } else {
                    for (Official officialTmp : officialsList) {
                        if (officialTmp.getOfficialIndices() == officialIndices) {
                            officeList.add(new Office(name, official));
                        }
                    }
                }
            }
        }
    }

    private void parseOfficials(JSONArray jArrayOfficials, List<Official> officialsList) throws JSONException {
        for (int i = 0; i < jArrayOfficials.length(); i++) {
            JSONObject jOfficial = (JSONObject) jArrayOfficials.get(i);
            String name = jOfficial.getString("name");
            JSONArray jArrayAddress;
            StringBuilder address = new StringBuilder();
            try {

                jArrayAddress = jOfficial.getJSONArray("address");
                for (int k = 0; k < 3; k++) {
                    try {
                        int index = k + 1;
                        address.append(((JSONObject) jArrayAddress.get(k)).getString("line" + index));
                        address.append("\n");
                    } catch (JSONException jse) {
                    }
                }
                address.append(((JSONObject) jArrayAddress.get(0)).getString("city"));
                address.append(", ");
                address.append(((JSONObject) jArrayAddress.get(0)).getString("state"));
                address.append(" ");
                address.append(((JSONObject) jArrayAddress.get(0)).getString("zip"));
            } catch (JSONException jse) {
            }
            if (address.toString().equals("")) {
                address.append("No Data Provided");
            }
            String party = null;
            try {
                party = jOfficial.getString("party");
            } catch (JSONException jse) {
            }
            String phone = null;
            try {
                JSONArray jArrayPhone = jOfficial.getJSONArray("phones");
                if (jArrayPhone != null) {
                    if (jArrayPhone.length() != 0) {
                        phone = jArrayPhone.get(0).toString();
                    }
                }
            } catch (JSONException jse) {
            }
            String url = null;
            try {
                JSONArray jArrayUrl = jOfficial.getJSONArray("urls");
                if (jArrayUrl != null) {
                    if (jArrayUrl.length() != 0) {
                        url = jArrayUrl.get(0).toString();
                    }
                }
            } catch (JSONException jse) {
            }
            String email = null;
            try {
                JSONArray jArrayEmail = jOfficial.getJSONArray("emails");
                if (jArrayEmail != null) {
                    if (jArrayEmail.length() != 0) {
                        email = jArrayEmail.get(0).toString();
                    }
                }
            } catch (JSONException jse) {
            }
            String photoUrl = null;
            try {
                photoUrl = jOfficial.getString("photoUrl");
            } catch (JSONException jse) {
            }
            String facebook = null;
            String google = null;
            String twitter = null;
            String youtube = null;
            try {
                JSONArray jArrayChannels = jOfficial.getJSONArray("channels");
                for (int j = 0; j < jArrayChannels.length(); j++) {
                    JSONObject jNetwork = (JSONObject) jArrayChannels.get(j);
                    switch (jNetwork.getString("type")) {
                        case "GooglePlus":
                            google = jNetwork.getString("id");
                            break;
                        case "Facebook":
                            facebook = jNetwork.getString("id");
                            break;
                        case "Twitter":
                            twitter = jNetwork.getString("id");
                            break;
                        case "YouTube":
                            youtube = jNetwork.getString("id");
                            break;
                        default:
                            break;
                    }
                }
            } catch (JSONException jse) {
            }
            officialsList.add(new Official(i, name, address.toString(), party, phone, url, email, photoUrl, google, facebook, twitter, youtube));
        }
    }
}
