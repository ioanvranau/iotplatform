var websocket;
var userToken;

$(document).ready(function (){
	//create a new WebSocket object.
	var wsDefaultUri = "ws://localhost:8086/prices";
	websocket = new WebSocket(wsDefaultUri);

	websocket.onopen = function(ev) { // connection is open
        initializeComponent();
	};
	if (getCookie("token") != ""){
            userToken = getCookie("token");
    }

	//#### Message received from server
	websocket.onmessage = function(ev) {
        $('#message').html = ev.data;
		var msg = JSON.parse(ev.data); //sends Json data
		var type = msg.type; //message type

        console.log("received -> " + ev.data);
		if(type == 'price')
		{
			$('#' + msg.code + "Price").text(msg.price);
			$('#' + msg.code + "Change").text(msg.changed);
			populatePriceChart(msg.code + "PriceChart", msg.price);
		}
		else if(type == 'system')
		{
			$('#message').html = umsg;
		}
		else if(type == 'token')
		{
		   userToken = msg.token;
		   setCookie("token", userToken);
           setCookie("username", msg.username);
		   window.location = "index.html";
		}
        else if(type == 'logout')
        {
            setCookie("username", "");
            setCookie("token","");
            window.location = "login.html";
        }
        else if(type == 'lock')
        {
            setCookie("token","");
            window.location = "locked.html";
        }
        else if(type == 'register')
        {
            $('#registerMessages').val("You have been successfully registered!");
        }
		else if(type == 'topics')
		{
            fillInTopics(msg);
		}
        else if(type == 'allDevices')
        {
            fillInDevices(msg);
        }
		else if(type == 'migrate')
		{
            alert(type);
            migrate(msg.serverAddress, msg.serverPort);
		}
		else if(type == 'topicSubscribe')
		{
//            var subscribeBtn = document.getElementById(msg.topicCode + "Subscribe");
//            subscribeBtn.innerHTML = "<button class=\"btn\" onclick=\"javascript:topicUnsubscribe('" + msg.topicCode +"')\">Unsubscribe</button>";

            var targetIDS1 = msg.topicCode + "Subscribe";
            var htmlS1 = "<button id=\"" + targetIDS1 +"\"class=\"btn\" onclick=\"javascript:topicUnsubscribe('" + msg.topicCode + "')\">Unsubscribe</button>";
            replaceTargetWith(targetIDS1, htmlS1);
		}
		else if(type == 'topicUnsubscribe')
        {
              var unSubscribeBtn = document.getElementById(msg.topicCode + "Subscribe");
            var targetID = msg.topicCode + "Subscribe";
            var html = "<button id=\"" + targetID +"\"class=\"btn\" onclick=\"javascript:topicSubscribe('" + msg.topicCode + "')\">Subscribe</button>";
            replaceTargetWith(targetID, html);
//              unSubscribeBtn.innerHTML = "<button class=\"btn\" onclick=\"javascript:topicSubscribe('" + msg.topicCode +"')\">Subscribe</button>";
        }
        else if(type == 'error')
        {
            $('#errorMessages').val(msg.message);
        }
        else if(type == 'userProfile')
        {
           fiilInUserProfile(msg)
        }
        else if(type == 'updateUserProfile')
        {
           window.location="userprofile.html";
        }
	};

	websocket.onerror	= function(ev){$('#message').append("<div class=\"system_error\">Error Occurred - "+ev.data+"</div>");};
	websocket.onclose 	= function(ev){$('#message').append("<div class=\"system_msg\">Connection Closed</div>");};
});

function replaceTargetWith( targetID, html ){
    /// find our target
    var i, tmp, elm, last, target = document.getElementById(targetID);
    /// create a temporary div or tr (to support tds)
    tmp = document.createElement(html.indexOf('<td')!=-1?'tr':'div');
    /// fill that div with our html, this generates our children
    tmp.innerHTML = html;
    /// step through the temporary div's children and insertBefore our target
    i = tmp.childNodes.length;
    last = target;
    while(i--){
        target.parentNode.insertBefore((elm = tmp.childNodes[i]), last);
        last = elm;
    }
    /// remove the target.
    target.parentNode.removeChild(target);
}

/////////////////////////////       SENT MESSAGES START         ///////////////////////////////////////////////

function register() {
          $('#errorMessages').val("");
          $('#registerMessages').val("");
          setCookie("token","");
          $('#message').html = "Connected!"; //notify user
          var username = $('#usernameInputTextId').val();
          var password = $('#passwordInputTextId').val();
          var email = $('#emailInputTextId').val();
          var msg = {
                    type:'register',
                    username : username,
                    password : password,
                    email : email
          };
          console.log("sent -> " + JSON.stringify(msg));
          websocket.send(JSON.stringify(msg));
}

function startListening(){
         //prepare json data
         var msg = {
                type: 'startListening',
                token: userToken
         };
         //convert and send data to server
         console.log("sent -> " + JSON.stringify(msg));
         websocket.send(JSON.stringify(msg));
}

function login() {
        $('#errorMessages').val("");
        setCookie("token","");
        $('#message').html = "Connected!"; //notify user
        var username = $('#usernameInputTextId').val();
        var password = $('#passwordInputTextId').val();
        var msg = {
            type:'login',
            username : username,
            password : password
        };
        console.log("sent -> " + JSON.stringify(msg));
        websocket.send(JSON.stringify(msg));
}

function unlock() {
    $('#errorMessages').val("");
    $('#message').html = "Connected!"; //notify user
    var username = $('#usernameDisplayId').text();
    var password = $('#passwordInputTextId').val();
    var msg = {
        type:'login',
        username : username,
        password : password
    };
    console.log("sent -> " + JSON.stringify(msg));
    websocket.send(JSON.stringify(msg));
}


function logout() {
    $('#errorMessages').val("");
    $('#message').html = "Disconnected!"; //notify user
    var msg = {
        type:'logout',
        token : getCookie("token")
    };
    console.log("sent -> " + JSON.stringify(msg));
    websocket.send(JSON.stringify(msg));
}

function lock() {
    $('#errorMessages').val("");
    $('#message').html = "Disconnected!"; //notify user
    var msg = {
        type:'lock',
        token : getCookie("token")
    };
    console.log("sent -> " + JSON.stringify(msg));
    websocket.send(JSON.stringify(msg));
}

function getAllTopics(){
         var topicsMsg = {
                type: 'allDevices',
                token : getCookie("token")
         };
         console.log("sent -> " + JSON.stringify(topicsMsg));
         websocket.send(JSON.stringify(topicsMsg));
}

function fillUserProfile(){
    var userProfMsg = {
        type: 'userProfile',
        token : getCookie("token")
    };
    console.log("sent -> " + JSON.stringify(userProfMsg));
    websocket.send(JSON.stringify(userProfMsg));
}


function disableAccount(){
    var disabMsg = {
        type: 'disableAccount',
        weeksNumber: $('#disableAccountId').val(),
        token : getCookie("token")
    };
    console.log("sent -> " + JSON.stringify(disabMsg));
    websocket.send(JSON.stringify(disabMsg));
    logout();

}

function discard() {
    window.location="userprofile.html";
}

function saveUserProfile() {
    var saveUserProfMsg = {
        type: 'updateUserProfile',
        name: $('#nameTxtId').val(),
        country: $("#simg").select2("val"),
        email: $('#emailTextId').val(),
        password: $('#passUserProfileTxtId').val(),
        token : getCookie("token")
    };
    console.log("sent -> " + JSON.stringify(saveUserProfMsg));
    websocket.send(JSON.stringify(saveUserProfMsg));
}

function migrate(serverAddress, serverPort){
      websocket = new WebSocket("ws://" + serverAddress + ":" + serverPort + "/prices");
      var migrateMsg = {
                    type: 'migrate',
                    username : username,
                    password : password,
                    email : email
      };
      console.log("sent -> " + JSON.stringify(migrateMsg));
      websocket.send(JSON.stringify(migrateMsg));
}

function topicSubscribe(topicCode){
        var topicSubscribeMsg = {
            type:'topicSubscribe',
            token:getCookie("token"),
            topicCode:topicCode
        };
        console.log("sent -> " + JSON.stringify(topicSubscribeMsg));
        websocket.send(JSON.stringify(topicSubscribeMsg));
}

function topicUnsubscribe(topicCode){
        var topicUnsubscribeMsg = {
            type:'topicUnsubscribe',
            token:getCookie("token"),
            topicCode:topicCode
        };
        console.log("sent -> " + JSON.stringify(topicUnsubscribeMsg));
        websocket.send(JSON.stringify(topicUnsubscribeMsg));
}
/////////////////////////////       SENT MESSAGES END         ////////////////////////////////////////////////

/////////////////////////////       LOGIC RELATED DATA         ///////////////////////////////////////////////
var pricesMap = new Object();

function getPriceData(priceChartId, price){
    //this is an array like initArray in fillInTopics
	var pricesArray = pricesMap[priceChartId];
	pricesArray.shift();
	pricesArray.push(price);
	return pricesArray;
}
function initPriceChart(priceChartId, initialPrice) {
    pricesMap[priceChartId] = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    populatePriceChart(priceChartId, initialPrice);
}

function populatePriceChart(priceChartId, price){
	var $el = $("#" + priceChartId),
	pricesArray = getPriceData(priceChartId, price);

	$el.sparkline(pricesArray, {
		width: ($("#left").width() > 200) ? 100 : $("#left").width() - 100,
		height: '25px',
		enableTagOptions: true
	});

	$el.prev().html(pricesArray[pricesArray.length - 1]);
}



function fiilInUserProfile(msg) {
    if(isEmpty(msg.name) ) {
        $('#nameTxtId').val("");
    } else{
        $('#nameTxtId').val(msg.name);
    }
    var countr = msg.country;
    if(isEmpty(countr) ) {
        $("#simg").select2("val", "NONE");
    } else {
        $("#simg").select2("val", msg.country);
    }
    if(isEmpty(msg.email) ) {
        $('#emailTextId').val("");
    } else {
        $('#emailTextId').val(msg.email);
    }

    if(isEmpty(msg.password) ) {
        $('#passUserProfileTxtId').val("");
    } else {
        $('#passUserProfileTxtId').val(msg.password);
    }

}


function isEmpty(myString) {
    return myString == null || myString == '';
}

function fillInTopics(msg) {
              var priceChart = "PriceChart";
              var topics = "";
               for (var i in msg.topics) {
                  var topic = msg.topics[i];
                  var priceChartId = topic.code + priceChart;
                  topics += "<tr><td>" + topic.code + "</td><td>" + topic.name + "</td><td><div id=\"" + topic.code + "Price\">" + topic.price + "</div></td>" +
                  "<td><span class=\"spark\" id=\"" + priceChartId + "\" sparkLineColor=\"#3a852e\" sparkFillColor=\"#a0d897\"></span></td>" +
                  "<td><div id=\"" + topic.code + "Change\">0</div></td>";
                   if (topic.defaultSubscribed == false) {
                        topics += "<td><button " + "id=\"" + topic.code + "Subscribe\"class=\"btn\" onclick=\"javascript:topicSubscribe('" + topic.code +"')\">Subscribe</button></td></tr>";
                   }
                   else {
                       topics += "<td><button " + "id=\"" + topic.code + "Subscribe\"class=\"btn\" onclick=\"javascript:topicUnsubscribe('" + topic.code +"')\">Unsubscribe</button></td></tr>";
                   }
              }
              var fieldNameElement = document.getElementById('userTopics');
              fieldNameElement.innerHTML = topics;
              for (var i in msg.topics) {
                var topic = msg.topics[i];
                var priceChartId = topic.code + priceChart;
                initPriceChart(priceChartId, topic.price);
              }
              startListening();
}

function fillInDevices(msg) {
    var topics = "";
    for (var i in msg.devices) {
        var topic = msg.devices[i];
        topics += "<tr><td>" + topic.id + "</td><td>" + topic.param1+ "</td>"
            ;

    }
    var fieldNameElement = document.getElementById('userTopics');
    fieldNameElement.innerHTML = topics;
    startListening();
}

/////////////////////////////////////////      COOKIE HANDLING        //////////////////////////////////////////////////
function setCookie(cname, cvalue) {
	var d = new Date();
	d.setTime(d.getTime() + (3 * 24 * 60 * 60 * 1000));
	var expires = "expires=" + d.toGMTString();
	document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for ( var i = 0; i < ca.length; i++) {
		var c = ca[i].trim();
		if (c.indexOf(name) == 0)
			return c.substring(name.length, c.length);
	}
	return "";
}