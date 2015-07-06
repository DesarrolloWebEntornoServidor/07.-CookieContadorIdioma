package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ContadorDeVisitas", urlPatterns = { "/ContadorDeVisitas" })
public class ContadorDeVisitas extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final static String PAGINA_LOGIN = "formularioContador.html";

	private String usuario;
	private String clave;

	private int contVisitas;
	private String bienvenida;
	private String ciclo;

	private Boolean hayCookie;
	private String valorCookie;

	LinkedHashMap<String, String> credenciales;
	private LinkedHashMap<String, String> arrayCiclos;

	protected void procesaSolicitud(HttpServletRequest request,
			HttpServletResponse response) {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();

			// Array asociativo de usuarios/claves
			iniciarArrayUsuarios();

			// Recepción de parámetros
			obtenerParametros(request);

			// Si el usuario se ha validado correctamente
			if (esUsuarioValido()) {
				// Recepción de cookies
				recibirCookie(request);

				// Array asociativo para generar el radio button con scriptlet
				iniciarArrayAsociativo();

				contarVisitas();

				// Salida
				imprimirPagina(out);
			} else { // Si el usuario no es válido, se vuelve a la ventana de
						// login
				redirigirPagina(request, response);
			}
		} catch (Exception e) {
			generarPaginaError(out, e);
		}
	}

	private void generarPaginaError(PrintWriter out, Exception e) {
		out.println("Se produce una excepción <br />");
		out.println(e.getMessage());
	}

	private void redirigirPagina(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request
				.getRequestDispatcher("PAGINA_LOGIN");
		rd.forward(request, response);
	}

	private void iniciarArrayUsuarios() {
		credenciales = new LinkedHashMap<String, String>();
		credenciales.put("alfa", "beta");
		credenciales.put("gamma", "delta");
	}

	private void contarVisitas() {
		// si no existe la cookie el contador de visitas se pone a 1
		contVisitas = 0;

		if (!hayCookie) { // si no existe la cookie => primera visita
			// valores por defecto
			contVisitas = 1;
			ciclo = "ASIR";
			bienvenida = "Bienvenid@ aprendiz";
		} else { // si existe la cookie se recupera el contador y se le
					// suma 1
			bienvenida = "Hola de nuevo " + usuario;
			String[] arrayValorCookie;
			arrayValorCookie = valorCookie.split(",");
			ciclo = arrayValorCookie[0];
			contVisitas = Integer.parseInt(arrayValorCookie[1]);
			contVisitas++;
		}
	}

	private void recibirCookie(HttpServletRequest request) {
		Cookie[] arrayCookies = request.getCookies();
		// Comprobar si el navegador tenía cookie del usuario
		hayCookie = false;
		valorCookie = "";
		if (arrayCookies != null) { // se recibe alguna cookie desde el
									// cliente
			int longitud = 0;
			longitud = arrayCookies.length;
			for (int i = 0; i < longitud; i++) {
				if (arrayCookies[i].getName().equals(usuario)) {
					hayCookie = true;
					valorCookie = arrayCookies[i].getValue();
				}
			}
		}

	}

	private void iniciarArrayAsociativo() {
		arrayCiclos = new LinkedHashMap<String, String>();
		arrayCiclos.put("ASIR", "ASIR");
		arrayCiclos.put("DAM", "DAM");
		arrayCiclos.put("DAW", "DAW");
	}

	private void imprimirPagina(PrintWriter out) {
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Contador de visitas</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>ContadorDeVisitas</h1>");
		out.println(bienvenida);
		out.println("<p>Indica tu ciclo preferido:</p>");
		out.println("<form name=\"f2\" action=\"GuardaCookie\" method=\"get\">");
		out.println("<input type=\"hidden\" name=\"usuario\" value=\""
				+ usuario + "\"></input>");
		out.println("<input type=\"hidden\" name=\"contVisitas\" value=\""
				+ contVisitas + "\"></input>");
		out.println(generaBotonesRadio("ciclo", arrayCiclos, ciclo)
				+ "<br /> <br />");
		out.println("Número de visitas= " + Integer.toString(contVisitas)
				+ "<br /> <br />");
		out.println("<input type=\"submit\" value=\"Desconectar\"></submit> ");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	private void obtenerParametros(HttpServletRequest request) {
		usuario = request.getParameter("usuario");
		clave = request.getParameter("clave");
	}

	private Boolean esUsuarioValido() {
		Boolean usuarioValido = false;
		for (String key : credenciales.keySet()) {
			if (usuario.equals(key)) {
				if (clave.equals(credenciales.get(key))) {
					usuarioValido = true;
				}
			}
		}
		return usuarioValido;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		procesaSolicitud(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		procesaSolicitud(request, response);
	}

	private String generaBotonesRadio(String nombreControl,
			LinkedHashMap<String, String> arrayCiclos, String valorSeleccionado) {
		String salida = "";
		Iterator<String> iteradorConjuntoClaves = arrayCiclos.keySet()
				.iterator();
		while (iteradorConjuntoClaves.hasNext()) {
			String clave = iteradorConjuntoClaves.next();
			String valor = arrayCiclos.get(clave);
			if (valorSeleccionado.equals(clave)) {
				salida += "<label>" + valor
						+ "</label><input type=\"radio\" name=\""
						+ nombreControl + "\" value=\"" + clave
						+ "\" checked=\"checked\" />" + "\n";
			} else {
				salida += "<label>" + valor
						+ "</label><input type=\"radio\" name=\""
						+ nombreControl + "\" value=\"" + clave + "\" />"
						+ "\n";
			}
		}
		return salida;
	}
}
