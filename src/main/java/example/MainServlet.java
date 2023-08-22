package example;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/hero"})
public class MainServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log("Method init");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.getWriter().write("Method service enter\n");
        super.service(req, resp);
//        resp.getWriter().write("Method service exit\n");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String uri = req.getRequestURI();
//        String params = formatParams(req);
//        resp.getWriter().write("Method doGet\nURI: " + uri + "\nParams:\n" + params + "\n");
        String resHero =  new JSONObject().put("hero",new JSONObject()
                                          .put("id",1)
                                          .put("name","Pudge")
                                          .put("lvl",1))
                                          .toString();
        resp.getWriter().write(resHero);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        JSONObject json = extractJson(req, resp);
        jsonModify(json);
        resp.getWriter().write(json.toString());
    }
    private static void jsonModify(JSONObject json) {
        try{
            JSONObject id = json.getJSONObject("hero");
            id.put("id",100);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private static JSONObject extractJson(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuffer sb = new StringBuffer();
        String line;
        JSONObject jsonObject = null;

        while ((line = reader.readLine()) != null)
            sb.append(line);
        try {
            jsonObject =  new JSONObject(sb.toString());
        } catch (JSONException e) {
            resp.getWriter().write(e.getMessage());
        }
        return jsonObject;
    }

    private String formatParams(HttpServletRequest req) {
        return req.getParameterMap()
                .entrySet()
                .stream()
                .map(entry -> {
                    String param = String.join(" and ", entry.getValue());
                    return entry.getKey() + " => " + param;
                })
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void destroy() {
        log("Method destroy");
    }
}
