
function refreshPage() {
	document.getElementById("refresh").click();
}

function popup(href) {
	window.open(href, "", "resizable,scrollbars");
}

function popupLink(linkId) {
	popup(document.getElementById(linkId).href);
}

function openLink(linkId) {
	window.open(document.getElementById(linkId).href);
}

/* ----- header ----- */

function header() {
	return window.parent.header.document;
}

function headerElement(elementName) {
	var h = header();
	if (!h) return;
	return h.getElementById(elementName);
}

function headerHide(elementName) {
	var e = headerElement(elementName);
	if (!e) return;
	e.style.display = "none";
}

function headerShow(elementName) {
	var e = headerElement(elementName);
	if (!e) return;
	e.style.display = "";
}

/* ----- /header ----- */

var renderTime = new Date();
var showSecs = false;
function checkTimeout(sessionTime) {
	elt = headerElement("session-timeout");
	if (!elt) return;
	remainingTime = Math.round(((renderTime - new Date()) + sessionTime*1000*60) / 1000);
	if (remainingTime < 0) {
		elt.value = "(-_-)";
		return; // fin
	} else if (remainingTime < 5*60) {
		elt.className = "session-timeout-red-alert";
		if (!showSecs) {
			showSecs = true;
			alert("Attention : la session expire dans moins de 5 minutes.");
		}
	} else if (remainingTime < 10*60) {
		elt.className = "session-timeout-warning";
	} else {
		elt.className = null;
	}
	
	setTimeout("checkTimeout("+sessionTime+")", 1000);
	
	mins = Math.floor(remainingTime / 60);
	secs = remainingTime - mins*60; if (secs < 10) secs = "0" + secs;
	if (showSecs) {
		elt.innerHTML = mins + ":" + secs;
	} else {
		elt.innerHTML = mins + "m";
	}
}

function documentClicked(event) {
//	if (event.target != null && event.target.tagName == "A" && event.button == 0) {
//		setProcessing();
//	}
}

function documentKeyDown(event) {
	if (event.keyCode != 8 &&
		event.keyCode != 116)
		return true;

	if (event.target) {
		target = event.target;
	} else if (event.srcElement) {
		target = event.srcElement;
	}

	if (target.tagName == 'INPUT')
		return true;
	if (target.tagName == 'TEXTAREA')
		return true;

	return false;
}

showProcessing = true;

function setProcessing() {
	if (!header()) return;
	
	if (!showProcessing) {
		showProcessing = true;
		return false;
	}
	headerShow("loading");
	
	headerElement("chrono").start();
	
	return true;
}

function hideProcessing() {
	showProcessing = false;
}

/* ----- */

function toggleDisplay(element) {
	if (element.style.display == "none") {
		element.style.display = "";
	} else {
		element.style.display = "none";
	}
	return false;
}

function tableauSelectAllChanged(checkbox) {
	tbody = checkbox.parentNode.parentNode.parentNode.nextSibling;
	checked = checkbox.checked;
	for (node = tbody.firstChild; node != null; node = node.nextSibling) {
		td = node.firstChild;
		if (td == null) continue;
		td.firstChild.checked = checked;
	}
}

function clickIfEnter(event, elementId) {
	if (event.keyCode != 13)
		return true;
	
	element = document.getElementById(elementId);
	element.click();
	
	event.cancelBubble = true;
	
	return false;
}

function disableButtons() {
	elts = document.getElementsByTagName("INPUT");
	for (i = 0; i < elts.length; i++) {
		e = elts[i];
		if (e.getAttribute('type') != 'submit')
			continue;
		e.disabled = true;
	}
	elts = document.getElementsByTagName("BUTTON");
	for (i = 0; i < elts.length; i++) {
		e = elts[i];
		e.disabled = true;
	}
}

// ----------------------------------------------------------------------------
// IE, toujours aussi fort.
// Aujourd'hui, nous allons lui apprendre "addEventListeners".

function createIEaddEventListeners()
{
    if (document.addEventListener || !document.attachEvent)
        return;

    function ieAddEventListener(eventName, handler, capture)
    {
        if (this.attachEvent)
            this.attachEvent('on' + eventName, handler);
    }

    function attachToAll()
    {
        var i, l = document.all.length;

        for (i = 0; i < l; i++)
            if (document.all[i].attachEvent)
                document.all[i].addEventListener = ieAddEventListener;
    }

    var originalCreateElement = document.createElement;

    document.createElement = function(tagName)
    {
        var element = originalCreateElement(tagName);
        
        if (element.attachEvent)
            element.addEventListener = ieAddEventListener;

        return element;
    }
    
    window.addEventListener = ieAddEventListener;
    document.addEventListener = ieAddEventListener;

    var body = document.body;
    
    if (body)
    {
        if (body.onload)
        {
            var originalBodyOnload = body.onload;

            body.onload = function()
            {
                attachToAll();
                originalBodyOnload();
            };
        }
        else
            body.onload = attachToAll;
    }
    else
        window.addEventListener('load', attachToAll);
}

createIEaddEventListeners();
