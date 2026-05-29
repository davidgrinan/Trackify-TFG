package API;

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
                                             String tipo,
                                             String genero,
                                             String estado,
                                             Integer valoracion) {
        JSONObject json = new JSONObject();

        try {
            json.put("titulo", titulo);
            json.put("tipo", tipo);
            json.put("genero", genero);
            json.put("estado", estado);

            if (valoracion != null) {
                json.put("valoracion", valoracion);
            }

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
        List<ContenidoModel> contenidos = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(responseJson);

            for (int i = 0; i < array.length(); i++) {
                contenidos.add(parseContenido(array.getJSONObject(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contenidos;
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
        String titulo = json.optString("titulo", "");
        String tipo = json.optString("tipo", "");
        String genero = json.optString("genero", "");
        String estado = json.optString("estado", "");

        Integer valoracion = null;

        if (!json.isNull("valoracion")) {
            valoracion = json.optInt("valoracion");
        }

        return new ContenidoModel(id, titulo, tipo, genero, estado, valoracion);
    }

    public static List<GeneroModel> parseArrayGeneros(String responseJson) {
        List<GeneroModel> generos = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(responseJson);

            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                generos.add(new GeneroModel(
                        json.optLong("id", json.optLong("codigo", -1)),
                        json.optString("nombre", "")
                ));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return generos;
    }

    public static List<TipoModel> parseArrayTipos(String responseJson) {
        List<TipoModel> tipos = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(responseJson);

            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                tipos.add(new TipoModel(
                        json.optLong("id", json.optLong("codigo", -1)),
                        json.optString("nombre", "")
                ));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tipos;
    }

    public static List<EstadoModel> parseArrayEstados(String responseJson) {
        List<EstadoModel> estados = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(responseJson);

            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                estados.add(new EstadoModel(
                        json.optLong("id", json.optLong("codigo", -1)),
                        json.optString("nombre", "")
                ));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return estados;
    }

    private UtilJSONParser() {
        throw new AssertionError();
    }
}