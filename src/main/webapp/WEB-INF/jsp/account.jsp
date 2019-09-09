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
            <div class="sidebar-heading"> User Page </div>
            <div class="list-group list-group-flush">
                <a href="#" class="list-group-item list-group-item-action bg-light" data-for-page="page1">Info</a>
                <a href="#" class="list-group-item list-group-item-action bg-light" data-for-page="page2">Devices</a>
                <a href="#" class="list-group-item list-group-item-action bg-light" data-for-page="page3">Users</a>
                <a href="#" class="list-group-item list-group-item-action bg-light" data-for-page="page4">Statistics</a>
                <a href="#" class="list-group-item list-group-item-action bg-light" data-for-page="page5">Help</a>
                <a href="#" class="list-group-item list-group-item-action bg-light" data-for-page="page6">Developers</a>
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
                            <a class="nav-link" href="#"> ${user.nickname} <span class="sr-only">(current)</span></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<c:url value="/logout" />">Logout</a>
                        </li>
                        <!-- <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Dropdown
                            </a>
                            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="#">Action</a>
                                <a class="dropdown-item" href="#">Another action</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="#">Something else here</a>
                            </div>
                        </li> -->
                    </ul>
                </div>
            </nav>

            <div class="container-fluid page" id="page1">
                <table class="table">
                    <tr>
                        <td>
                            Nickname :
                        </td>
                        <td>
                            ${account.nickname}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Email :
                        </td>
                        <td>
                            ${account.email}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            TOKEN :
                        </td>
                        <td>
                            ${account.token}
                        </td>
                    </tr>
                </table>
            </div>

            <div class="container-fluid page" id="page2">
                <table class="table" id="devices-table">
                    <thead>
                        <tr>
                            <th>Device Number</th>
                            <th>Device Key</th>
                            <th>Online</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="count" value="0" scope="page" />
                        <c:forEach items="${account.devices}" var="device">
                            <tr>
                                <td class="deviceCount">
                                    <c:set var="count" value="${count + 1}" scope="page" />
                                    ${count}
                                </td>
                                <td class="deviceKey">
                                    ${device.key}
                                </td>
                                <td>
                                    <c:if test="${device.connected == true}">
                                        <span class="dot green"></span>
                                    </c:if>

                                    <c:if test="${device.connected == false}">
                                        <span class="dot red"></span>
                                    </c:if>

                                </td>
                                <td>
                                    <button class="btn btn-danger removeDevice">Remove Device</button>
                                </td>
                            </tr>
                        </c:forEach>

                    </tbody>
                </table>
                <table class="table">
                    <tr>
                        <td>
                            New Device :
                        </td>
                        <td>
                            <div class="form-group">
                                <input type="text" class="form-control" id="deviceKeyInput"
                                    placeholder="Enter new device key">
                            </div>
                        </td>
                        <td>
                            <button class="btn btn-primary btn-successs" id="btnAddDeviceKey">Add</button>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="container-fluid page" id="page3">
                <table class="table" id="users-table">
                    <thead>
                        <tr>
                            <th>User Number</th>
                            <th>User Key</th>
                            <th>Online</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="count" value="0" scope="page" />
                        <c:forEach items="${account.users}" var="device">
                            <tr>
                                <td class="deviceCount">
                                    <c:set var="count" value="${count + 1}" scope="page" />
                                    ${count}
                                </td>
                                <td class="deviceKey">
                                    ${device.key}
                                </td>
                                <td>
                                    <c:if test="${device.connected == true}">
                                        <span class="dot green"></span>
                                    </c:if>

                                    <c:if test="${device.connected == false}">
                                        <span class="dot red"></span>
                                    </c:if>

                                </td>
                                <td>
                                    <button class="btn btn-danger removeUser">Remove User</button>
                                </td>
                            </tr>
                        </c:forEach>

                    </tbody>
                </table>
                <table class="table">
                    <tr>
                        <td>
                            New User :
                        </td>
                        <td>
                            <div class="form-group">
                                <input type="text" class="form-control" id="userKeyInput"
                                    placeholder="Enter new user key">
                            </div>
                        </td>
                        <td>
                            <button class="btn btn-primary btn-successs" id="btnAddUserKey">Add</button>
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

                    <ul>
                        <li>
                            Set up the mobile app that you are using by entering the
                            <ol>
                                <li>
                                    Auto generated TOKEN
                                </li>
                                <li>
                                    User key
                                </li>
                            </ol>
                            The user key is the unique identifier of a mobile device.<br>
                            Currently, you can Control your IoT devices from 4 different mobile device. (for each
                            account)<br>
                        </li>
                        <li>
                            Create new Devices in the Devices Tab, by giving each device a Unique 'Key' (of 10
                            characters).
                            <br>
                            Currently, you can create up to 4 devices per account.
                        </li>
                        <li>
                            Configure the TOKEN on the IoT device and the "Device Key" for each device.
                        </li>
                        <li>
                            After setting up the required codes, you can access anytime the Devices Tab to check the
                            availability of your devices.
                        </li>
                        <li>
                            Communication can only be made between a device and user. Users cannot communicate together.
                            Same for devices.
                        </li>
                    </ul>
                </p>
            </div>

            <div class="container-fluid page" id="page6">
                <h2>Developers</h2>
                <p>
                    <ul>
                        <li>
                            You should allow users to sign up for an account on this website so you clients devices dont conflic.
                        </li>
                        <li>
                            Unity Help:
                            <ol>
                                <li>
                                    Initialize the session : <br>
                                    <code>
                                        session = new ClientSession("API Token Here", "Device Code Here");<br>
                                    </code>
                                </li>
                                <li>
                                    Set the events :<br>
                                    <code>
                                        session.OnConnected += Connected;<br>
                                        session.onMessageReady += Receive;<br>
                                        session.OnConnectionFailed += Failed;<br>
                                    </code>
                                    Where <code>Connected</code>, <code>Receive</code> and <code>Failed</code> are function to be defined
                                </li>
                                <li>
                                    For sending Messages, build a message for sending it.<br>
                                    <code>
                                        Message m = new MessageBuilder()<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    .setMessgeType(MessageType.DATA)<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    .setMessage("HELLO")<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    .setToDeviceAddress("DESTINATION DEVICE CODE")<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    .build();<br>
                                        session.sendMessage(m);<br>
                                    </code>
                                </li>
                                <li>
                                    To send binary data, build a binary message:<br>
                                    <code>
                                        byte[] messageBytes = {0x62, 0x69, 0x6e, 0x61, 0x72, 0x79, 0x20, 0x74, 0x65, 0x78, 0x74};<br>
                                        Message msg = new MessageBuilder()<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    .setMessgeType(MessageType.DATA)<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    .setMessage(messageBytes)<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    .setToDeviceAddress("DESTINATION DEVICE CODE")<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    .build();<br>
                                        session.sendMessage(msg);<br>
                                    </code>
                                </li>
                                <li>
                                    To Receive a message: <br>
                                    <code>
                                        public void Receive(Message message){<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                            switch(message.getType()){<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                case MessageType.ERROR:<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    Debug.LogWarning(message.getDataAsString());<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    break;<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                case MessageType.DATA:<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                case MessageType.BROADCAST:<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    Debug.Log(message.getDataAsString());<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    break;<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                            }<br>
                                        }<br>
                                    </code>
                                </li>
                                <li>
                                    <span clas="red">Important note</span>, before to forget to disconnect when distroying the scene:<br>
                                    <code>
                                        public void OnDestroy(){<br>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                            session.Disconnect();<br>
                                        }<br>
                                    </code>
                                </li>
                                <li>
                                    <code>session.isConnected()</code> checks if you are still connected or not.
                                </li>
                            </ol>
                        </li>
                        <li>
                            Arduino Helper:
                            <ol>
                                <li>
                                    Create a WiFiClient Object and DeviceSession object as follow:<br>
                                    <code>
                                        WiFiClient client;<br>
                                        DeviceSession session(client, "API TOKEN", "DEVICE CODE");
                                    </code>
                                </li>
                                <li>
                                    Connect to WIFI: <br>
                                    <code>
                                        WiFi.mode(WIFI_STA);<br>
                                        WiFi.begin(SSID, PASSWORD);<br>
                                        while(WiFi.status() != WL_CONNECTED);<br>
                                    </code>
                                </li>
                                <li>
                                    Subscribe to receiving messages:<br>
                                    <code>
                                        session.setOnMessageReceived(OnMessageReceived);<br>
                                    </code>
                                    and <code>OnMessageReceived</code> is defined as follow :<br>
                                    <code>
                                        void OnMessageReceived(Message * message){<br>
                                            &nbsp;&nbsp;&nbsp;&nbsp;int length = message->getDataLength();
                                            &nbsp;&nbsp;&nbsp;&nbsp;char * data = message->getData();
                                            &nbsp;&nbsp;&nbsp;&nbsp; for(int i=0; i&#60;length; i++)
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Serial.println(data[i]);
                                            &nbsp;&nbsp;&nbsp;&nbsp;Serial.println("");
                                        }
                                    </code>
                                </li>
                                <li>
                                    Connect the session: <br>
                                    <code>
                                        bool result = session.connect();<br>
                                        while(!result); //cannot initiate session <br>
                                    </code>
                                </li>
                                <li>
                                    In the loop function, call :<br>
                                    <code>
                                        session.update();
                                    </code>
                                </li>
                                <li>
                                    To send Message:<br>
                                    <code>
                                        int messageToSendLength = 15;<br>
                                        bool keepAlive = true; // if set to false, the connection will be closed after this message.<br>
                                        Message m(DATA, "DeviceCode", "Message to send", 15, keepAlive);<br>
                                        session.sendMessage(m);
                                    </code>
                                </li>
                            </ol>
                        </li>
                    </ul>
                </p>
            </div>
        </div>

    </div>

    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>

    <script src="<c:url value="/resources/js/account.js" />" >
    </script>
</body>

</html>