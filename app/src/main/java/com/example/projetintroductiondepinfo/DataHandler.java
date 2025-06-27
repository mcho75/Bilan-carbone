package com.example.projetintroductiondepinfo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetintroductiondepinfo.Database.AppDatabase;
import com.example.projetintroductiondepinfo.Database.Emission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class DataHandler {

    Context context;
    public void DataRetriever(Context context) {
        this.context = context;

        // ensemble des requêtes que nous souhaitons réaliser
        HashMap<String,String> ajouts = new HashMap<>();
        ajouts.put("https://impactco2.fr/api/v1/thematiques/ecv/2", "alimentation");
        ajouts.put("https://impactco2.fr/api/v1/thematiques/ecv/3", "alimentation");
        ajouts.put("https://impactco2.fr/api/v1/thematiques/ecv/9", "alimentation");
        ajouts.put("https://impactco2.fr/api/v1/thematiques/ecv/4", "transport");
        ajouts.put("https://impactco2.fr/api/v1/thematiques/ecv/5", "bien");

        // Initialisation de la pile de requêtes
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // lancements successifs des requêtes
        for (String adresse : ajouts.keySet()) {
            Response.Listener<String> listener = new carbonListener(ajouts.get(adresse));
            Response.ErrorListener errorListener = new carbonErrorListener();
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    adresse,
                    listener,
                    errorListener
            );
            requestQueue.add(request);
        }
    }

    private class carbonListener implements Response.Listener<String> {

        String category;
        public carbonListener(String category) {
            this.category = category;
        }

        @Override
        public void onResponse(String response) {
            AppDatabase db = AppDatabase.getDatabase(context);

            // on deserialise la réponse JSON
            try {
                JSONObject get = new JSONObject(response);
                JSONArray data = get.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {
                    JSONObject o = data.getJSONObject(i);

                    /* TODO Pour l'instant, il n'y a pas de sous-catégorie récupérée.
                    *   À l'aide d'autres requêtes aux API dédiées, on pourrait les obtenir,
                    *   mais c'est plus long et plus complexe.
                    *   Exemple de requête avec sous-catégorie mais sans nom humain :
                    *   https://impactco2.fr/api/v1/alimentation?category=group
                    *   Pour les fruits et légumes, on peut personnaliser selon l'année,
                    *   mais ce sera plus complexe.
                    */
                    Emission emission = new Emission(
                            category,
                            null,
                            o.getString("slug"),
                            new String(o.getString("name").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8),
                            o.getDouble("ecv")
                    );

                    // On enregistre les émissions reçues
                    new Thread(
                            () -> db.emissionDAO().insert(emission)
                    ).start();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class carbonErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            throw new RuntimeException(error);
        }
    }
}
