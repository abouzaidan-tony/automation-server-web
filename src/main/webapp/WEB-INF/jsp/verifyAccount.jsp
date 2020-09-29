<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>Sign Up</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--===============================================================================================-->
    <link rel="icon" type="image/png" href="<c:url value="/resources/images/icons/favicon.ico" />" />
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css"
        href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css"
        href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css"
        href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.7.2/animate.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css"
        href="https://www.cursinhoativojaboticabal.com.br/inscricao/vendor/css-hamburgers/hamburgers.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css"
        href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.10/css/select2.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/util.css" />">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/main.css" />">
    <!--===============================================================================================-->
</head>

<body>

    <div class="limiter">
        <div class="container-login100"
            style="background-image: url('https://colorlib.com/etc/lf/Login_v12/images/img-01.jpg');">
            <div class="wrap-login100">
                <div class="login100-pic js-tilt" data-tilt>
                    <img src="<c:url value="/resources/images/img-01.png" />" alt="IMG">
                </div>

                <form:form method="POST" modelAttribute="verifyForm" class="login100-form validate-form" >
                    <span class="login100-form-title">
                        Verify Account
                    </span>

                    <span>
                        <!-- A code is sent to your email, enter it here:<br>
                        Please check your junk box too<br>&nbsp;<br>&nbsp;<br> -->
                        Currently, verification process for developers is done manually,<br>
                        Send an email to <a href="mailto:abouzaidan.tony@email.com">abouzaidan.tony@email.com</a> <br>
                        to get your OTP.
                    </span>

                    <div class="wrap-input100 validate-input" data-validate="Minimum length is 3">
                        <form:input path="userCode" class="input100" type="number" name="code" placeholder="Code" />
                        <span class="focus-input100"></span>
                        <span class="symbol-input100">
                            <i class="fa fa-lock" aria-hidden="true"></i>
                        </span>
                    </div>

                    <ul>
                        <c:if test="${not empty error}">
                            <li style="color:red">${error}</li>
                        </c:if>
                        <c:if test="${not empty errors}">
                            <c:forEach items="${errors}" var="er">
                                <li style="color:red">${er.value}</li>
                            </c:forEach>
                        </c:if>
                    </ul>
                
                    <div class="container-login100-form-btn">
                        <button class="login100-form-btn">
                            Verify
                        </button>
                    </div>

                    <div class="text-center p-t-136">
                        <a class="txt2" href="<c:url value="/login" /> ">
                        Home
                        <i class="fa fa-long-arrow-right m-l-5" aria-hidden="true"></i>
                        </a>
                    </div>

                </form:form>

            </div>
        </div>
    </div>



    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.10/js/select2.min.js"></script>
    <script src="https://unpkg.com/tilt.js@1.1.21/dest/tilt.jquery.min.js"></script>
    <script>
        $('.js-tilt').tilt({
            scale: 1.1
        })
    </script>
    <!--===============================================================================================-->
    <script src="<c:url value="/resources/js/main.js" />" ></script>

</body>

</html>