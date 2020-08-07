<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="${_csrf.parameterName}" content="${_csrf.token}" />

    <title>User Page</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css"
        href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">

    <link href="<c:url value="/resources/css/user.css" />" rel="stylesheet">

</head>

<body>

    <div class="d-flex" id="wrapper">

        <!-- Sidebar -->
        <div class="bg-light border-right" id="sidebar-wrapper">
            <div class="sidebar-heading"> Developer Page </div>
            <div class="list-group list-group-flush">
                <a href="#" class="list-group-item list-group-item-action bg-light" data-for-page="page1">Applications</a>
                <a href="#" class="list-group-item list-group-item-action bg-light" data-for-page="page4">Statistics</a>
                <a href="#" class="list-group-item list-group-item-action bg-light" data-for-page="page5">Help</a>
                <a href="#" class="list-group-item list-group-item-action bg-light" data-for-page="page6">Developers</a>
                <a href="<c:url value="/resources/plugin/wifiplugin.unitypackage" />" target="_blank" class="list-group-item list-group-item-action bg-light">Download Plugin</a>
                <!-- <a href="#" class="list-group-item list-group-item-action bg-light">Overview</a>
                <a href="#" class="list-group-item list-group-item-action bg-light">Events</a>
                <a href="#" class="list-group-item list-group-item-action bg-light">Profile</a>
                <a href="#" class="list-group-item list-group-item-action bg-light">Status</a> -->
            </div>
        </div>
        <!-- /#sidebar-wrapper -->

        <!-- Page Content -->
        <div id="page-content-wrapper">

            <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
                <button class="btn btn-primary menu-toggle">Hide Menu</button>

                <button class="navbar-toggler" type="button" data-toggle="collapse"
                    data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
                    aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav ml-auto mt-2 mt-lg-0">
                        <li class="nav-item active">
                            <a class="nav-link" href="#"> ${user.email} <span class="sr-only">(current)</span></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<c:url value="/logout" />">Logout</a>
                        </li>
                    </ul>
                </div>
            </nav>

            <div class="container-fluid page" id="page1">
                <table class="table" id="apps-table">
                    <thead>
                        <tr>
                            <th>Application Number</th>
                            <th>Application Token</th>
                            <th>Application Name</th>
                            <th>Online Users</th>
                            <th>Online Devices</th>
                            <th>Total Online Sessions</th>
                            <th>Total User Subscriptions</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="count" value="0" scope="page" />
                        <c:forEach items="${account.applications}" var="app">
                            <tr>
                                <td class="appCount">
                                    <c:set var="count" value="${count + 1}" scope="page" />
                                    ${count}
                                </td>
                                <td class="appToken">
                                    ${app.token}
                                </td>
                                <td>
                                    ${app.name}
                                </td>
                                <td>
                                    ${app.onlineUsers}
                                </td>
                                <td>
                                    ${app.onlineDevices}
                                </td>
                                <td>
                                    ${app.totalSessions}
                                </td>
                                <td>
                                    ${app.totalSubscriptions}
                                </td>
                                <td>
                                    <button class="btn btn-danger removeApp">Remove App</button>
                                </td>
                            </tr>
                        </c:forEach>

                    </tbody>
                </table>
                <table class="table">
                    <tr>
                        <td>
                            New Application :
                        </td>
                        <td>
                            <div class="form-group">
                                <input type="text" class="form-control" id="appNameInput"
                                    placeholder="Enter new application name">
                            </div>
                        </td>
                        <td>
                            <button class="btn btn-primary btn-successs" id="btnAddAppName">Add</button>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="container-fluid page" id="page4">
                Soon...
            </div>

            <div class="container-fluid page" id="page5">
                <p>
                    Thank you for choosing this service. <br><br>

                    This platform allows you to control your IoT device over the internet from you mobile device.<br>

                    What you have to do ? <br>

                   
                    
                </p>
            </div>

            <div class="container-fluid page" id="page6">
                <h2>Developers</h2>
                <p>
                    <ul>
                        <li>
                            You should allow users to sign up for an account on this website so you clients devices dont conflic.
                        </li>
                    </ul>
                </p>        

    </div>

    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>

    <script src="<c:url value="/resources/js/dev_account.js" />" >
    </script>
</body>

</html>