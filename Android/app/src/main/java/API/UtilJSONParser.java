package API;

import com.example.trackify.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UtilJSONParser {

    public static JSONObject createLogin(String nombreUsuario, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("nombreUsuario", nombreUsuario);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject createRegister(String nombreUsuario, String password, String email) {
        JSONObject json = new JSONObject();
        try {
            json.put("nombreUsuario", nombreUsuario);
            json.put("password", password);
            json.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject createContenido(String titulo,
                                             String descripcion,
                                             String genero,
                                             String tipo,
                                             String estado,
                                             Integer valoracion,
                                             String imagenUrl) {
        JSONObject json = new JSONObject();

        try {
            json.put("titulo", titulo);
            json.put("descripcion", descripcion);
            json.put("genero", genero);
            json.put("tipo", tipo);
            json.put("estado", estado);
            json.put("valoracion", valoracion);
            json.put("imagenUrl", imagenUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static String parseToken(String responseJson) {
        try {
            JSONObject json = new JSONObject(responseJson);
            return json.optString("data", null);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<ContenidoModel> parseArrayContenidos(String responseJson) {
        List<ContenidoModel> lista = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(responseJson);

            for (int i = 0; i < array.length(); i++) {
                lista.add(parseContenido(array.getJSONObject(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static ContenidoModel parseContenido(String responseJson) {
        try {
            return parseContenido(new JSONObject(responseJson));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ContenidoModel parseContenido(JSONObject json) {
        long id = json.optLong("id", json.optLong("codigo", -1));
        String imagenUrl = json.optString("imagenUrl", "");
        String titulo = json.optString("titulo", "");
        String tipo = json.optString("tipo", "");
        String genero = json.optString("genero", "");
        String estado = json.optString("estado", "");
        String descripcion = json.optString("descripcion", "");

        Integer valoracion = null;
        if (!json.isNull("valoracion")) {
            valoracion = json.optInt("valoracion");
        }

        return new ContenidoModel(
                id,
                titulo,
                tipo,
                genero,
                estado,
                descripcion,
                valoracion,
                imagenUrl
        );
    }

    public static JSONObject createCambiarPassword(String nuevaPassword) {
        JSONObject json = new JSONObject();

        try {
            json.put("nuevaPassword", nuevaPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
    private UtilJSONParser() {
        throw new AssertionError();
    }
}