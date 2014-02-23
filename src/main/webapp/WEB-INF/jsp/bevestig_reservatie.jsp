<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt'%>
<c:set var='contextPath'
	value='${pageContext.servletContext.contextPath}' />
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="Stijn Vansieleghem">

<title>CultuurHuis - Bevestig reservatie</title>

<!-- Bootstrap core CSS -->
<link href="${contextPath}/stylesheets/bootstrap.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="${contextPath}/stylesheets/justified-nav.css"
	rel="stylesheet">

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>
<body>
	<c:url value="voorstellingen" var="voorstellingenURL" />
	<c:url value="reservatiemandje" var="reservatiemandjeURL" />
	<c:url value="bevestig_reservatie" var="bevestig_reservatieURL" />
	<div class="container">


		<div class="masthead">
			<h3 class="text-muted">
				<a href="" title="Bevestiging reservaties">Het CultuurHuis -
					Bevestiging reservaties</a>
			</h3>

			<ul class="nav nav-justified">
				<li><a href="${voorstellingenURL}" title="Voorstellingen">Voorstellingen</a></li>
				<li><a href="${reservatiemandjeURL}" title="Reservatiemandje">Reservatiemandje</a></li>
				<c:if test="${not empty gebruikerGegevens}">
					<li><a href="<c:url value='/j_spring_security_logout'/>"
						title="Afmelden">Afmelden</a></li>
				</c:if>
			</ul>
		</div>

		<div class="row">
			<h2>Stap 1: wie ben je?</h2>
			<form action="<c:url value="/j_spring_security_check"/>"
				method="post" name="frmZoekGebruiker">
				<label>Gebruikersnaam:<br /> <input maxlength="50"
					id="gebruikersnaamVeld" name="j_username" type="text" autofocus
					required /></label><br /> <label>Paswoord<br /> <input
					maxlength="50" id="paswoordVeld" name="j_password" type="password"
					required /></label><br />
				<input id="zoekGebruiker" name="zoekGebruiker" type="submit"
					value="Zoek me op" />
			</form>
			<br />
			<form action="${bevestig_reservatieURL}" method="post"
				name="frmNieuweGebruiker">
				<input id="nieuweGebruiker" name="nieuweGebruiker" type="submit"
					value="Ik ben nieuw" />
			</form>
			<c:if test="${not empty gebruikerGegevens}">
				<p>
					<span class="bold"><c:out value="${gebruikerGegevens}" /></span>
				</p>
			</c:if>
			<c:if test="${not empty fouten}">
				<c:forEach var="fout" items="${fouten}">
					<ul class="fouten">
						<li>${fout}</li>
					</ul>
				</c:forEach>
			</c:if>
			<c:if test="${not empty gebruikerGegevens}">
				<h2>Stap 2:Bevestigen</h2>
				<form action="${bevestig_reservatieURL}" method="post">
					<input id="bevestigKnop" name="bevestig" type="submit"
						value="Bevestigen" />
				</form>
			</c:if>
		</div>

		<!-- Site footer -->
		<div class="footer">
			<p>
				&copy; Stijn Vansieleghem
				<jsp:useBean id="date" class="java.util.Date" />
				<fmt:formatDate value="${date}" pattern="yyyy" />
			</p>
		</div>

	</div>
	<!-- /container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<c:choose>
		<c:when test="${empty gebruikerGegevens}">
			<script>
				document.getElementById("gebruikersnaamVeld").disabled = false;
				document.getElementById("paswoordVeld").disabled = false;
				document.getElementById('zoekGebruiker').disabled = false;
				document.getElementById('nieuweGebruiker').disabled = false;
				document.getElementById('bevestigKnop').disabled = true;
			</script>
		</c:when>
		<c:otherwise>
			<script>
				document.getElementById("gebruikersnaamVeld").disabled = true;
				document.getElementById("paswoordVeld").disabled = true;
				document.getElementById('zoekGebruiker').disabled = true;
				document.getElementById('nieuweGebruiker').disabled = true;
				document.getElementById('bevestigKnop').disabled = false;
			</script>
		</c:otherwise>
	</c:choose>
</body>
</html>