<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<% session.invalidate(); %>

<c:redirect url="/login" />