<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<body>
		up<br/>
		<c:forEach items="${skus}" var="sku">
		    ${sku.id}<br/>
		</c:forEach>
	</body>
</html>