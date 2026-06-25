package API;

import org.json.JSONObject;

public class API {

    private static final String BASE_URL = "http://192.168.0.14:8080/";

    public static void login(JSONObject usuario, UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(
                UtilREST.QueryType.POST,
                BASE_URL + "auth/login",
                usuario.toString(),
                listener
        );
    }

    public static void register(JSONObject usuario, UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(
                UtilREST.QueryType.POST,
                BASE_URL + "auth/register",
                usuario.toString(),
                listener
        );
    }

    public static void listarContenido(String token, UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(
                UtilREST.QueryType.GET,
                BASE_URL + "api/contenido/listarporusuario",
                null,
                token,
                listener
        );
    }

    public static void obtenerContenido(long id, String token, UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(
                UtilREST.QueryType.GET,
                BASE_URL + "api/contenido/" + id,
                null,
                token,
                listener
        );
    }

    public static void crearContenido(JSONObject contenido, String token, UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(
                UtilREST.QueryType.POST,
                BASE_URL + "api/contenido",
                contenido.toString(),
                token,
                listener
        );
    }

    public static void actualizarContenido(long id, JSONObject contenido, String token, UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(
                UtilREST.QueryType.PUT,
                BASE_URL + "api/contenido/" + id,
                contenido.toString(),
                token,
                listener
        );
    }

    public static void eliminarContenido(long id, String token, UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(
                UtilREST.QueryType.DELETE,
                BASE_URL + "api/contenido/" + id,
                null,
                token,
                listener
        );
    }

    public static void filtrarContenido(String tipo,
                                        String genero,
                                        String estado,
                                        Integer valoracion,
                                        String titulo,
                                        String token,
                                        UtilREST.OnResponseListener listener) {

        try {
            StringBuilder url = new StringBuilder(BASE_URL + "api/contenido/filtros?");

            if (tipo != null && !tipo.isEmpty()) {
                url.append("tipo=").append(java.net.URLEncoder.encode(tipo, "UTF-8")).append("&");
            }

            if (genero != null && !genero.isEmpty()) {
                url.append("genero=").append(java.net.URLEncoder.encode(genero, "UTF-8")).append("&");
            }

            if (estado != null && !estado.isEmpty()) {
                url.append("estado=").append(java.net.URLEncoder.encode(estado, "UTF-8")).append("&");
            }

            if (valoracion != null) {
                url.append("valoracion=").append(valoracion).append("&");
            }

            if (titulo != null && !titulo.isEmpty()) {
                url.append("titulo=").append(java.net.URLEncoder.encode(titulo, "UTF-8")).append("&");
            }

            UtilREST.runQuery(
                    UtilREST.QueryType.GET,
                    url.toString(),
                    null,
                    token,
                    listener
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listarGeneros(String token, UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(
                UtilREST.QueryType.GET,
                BASE_URL + "api/genero",
                null,
                token,
                listener
        );
    }

    public static void listarTipos(String token, UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(
                UtilREST.QueryType.GET,
                BASE_URL + "api/tipo",
                null,
                token,
                listener
        );
    }

    public static void listarEstados(String token, UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(
                UtilREST.QueryType.GET,
                BASE_URL + "api/estado",
                null,
                token,
                listener
        );
    }

    public static void cambiarPassword(JSONObject password,
                                       String token,
                                       UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(
                UtilREST.QueryType.PUT,
                BASE_URL + "api/usuario/password",
                password.toString(),
                token,
                listener
        );
    }
}