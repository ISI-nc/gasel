

// Fonctions utilitaires

padWithZero = function(num, length) {
	text = num.toString();
	while (text.length < length) text = "0" + text;
	return text;
}

String.prototype.trim = function() {
	return this.replace(/(?:^\s+|\s+$)/g, "");
}

function Point(x, y) {
   this.x = x;
   this.y = y;
}

getPoint = function(elt) {
   var tag = elt;
   var point = new Point(0,0);
  
   do {
      point.x += tag.offsetLeft;
      point.y += tag.offsetTop;
      tag = tag.offsetParent;
   } while (tag.tagName != "BODY" && tag.tagName != "HTML");

   return point;
}

/* ****************************************************************************
** DateComponent
*/

DateComponent.MONTH_NAMES = [ "Janvier", "F&eacute;vrier", "Mars", "Avril",
	"Mai", "Juin", "Juillet", "Ao&ucirc;t", "Septembre", "Octobre", "Novembre",
	"D&eacute;cembre" ];

function DateComponent(clientId) {
	this._clientId = clientId;
	this._field = document.getElementById(clientId);
	this._icon = document.getElementById(clientId + "_icon");
	this._cal = document.getElementById(clientId + "_cal");
	this._table = document.getElementById(clientId + "_table");
	this._mois = document.getElementById(clientId + "_mois");
	this._annee = document.getElementById(clientId + "_annee");
	this.calendarVisible = (this._cal.style.display != 'none');
	this._ie = document.all ? true : false;
	this.updateDate();
}

DateComponent.prototype.showCalendar = function() {
	this.updateCalendar();
	this._cal.style.display = "block";
	this._cal.style.zIndex = 400;
	this.calendarVisible = true;
	if (this._ie) {
	  	dw = this._cal.offsetWidth;
	  	dh = this._cal.offsetHeight;
	  	
	  	p = getPoint(this._field);
		this._cal.style.top = (p.y + this._field.offsetHeight + 1) + "px";
		this._cal.style.left = p.x + "px";
		
     	var body = document.getElementsByTagName("body")[0];
     	if( !body ) return;

	 	//paste iframe under the modal
	   var underDiv = this._cal.cloneNode(false);
	   underDiv.style.zIndex = "390";
	   underDiv.style.margin = "0px";
	   underDiv.style.padding = "0px";
	   underDiv.style.display = "block";
	   underDiv.style.border = "none";
	   underDiv.style.width = dw;
	   underDiv.style.height = dh;
	   underDiv.innerHTML = "<iframe width=\"100%\" height=\"100%\" src='about:blank' frameborder=\"0\"></iframe>";
	   this._cal.parentNode.appendChild(underDiv);
	   this._underDiv = underDiv;
	}
}

DateComponent.prototype.hideCalendar = function() {
	this._cal.style.display = "none";
	this.calendarVisible = false;
	if (this._ie) {
	    if (this._underDiv) this._underDiv.removeNode(true);
	}
}

DateComponent.prototype.toggleCalendar = function() {
	if (this._cal.style.display == "none") {
		this.showCalendar();
	} else {
		this.hideCalendar();
	}
}

DateComponent.prototype.updateDate = function() {
	this.setDate(this._parse());
}

DateComponent.prototype.getDate = function() { return this.date; }
DateComponent.prototype.setDate = function(date) {
	this.date = date;
	this.updateField();
	if (this.calendarVisible)
		this.updateCalendar();
}
DateComponent.prototype.requireDate = function() {
	d = this.getDate();
	return d == null ? new Date() : d;
}

// cf GaselDateTranslator
DateComponent.prototype._parse = function() {
	text = this._field.value.trim();
	if (text.match(/^$/)) {
		this._field.value = "";
		return null;
	}
	
	if (text.match(/^\d{6}$/)) {
		j = this._parseInt(text.substring(0, 2));
		m = this._parseInt(text.substring(2, 4)) - 1;
		a = this._parseInt(text.substring(4, 6));
		a = this._convertYear(a);
		return this._composeDate(a, m, j);
	}

	if (text.match(/^\d{8}$/)) {
		j = this._parseInt(text.substring(0, 2));
		m = this._parseInt(text.substring(2, 4)) - 1;
		a = this._parseInt(text.substring(4, 8));
		return this._composeDate(a, m, j);
	}

	if (text.match(/^\d{1,2}[\./-]\d{1,2}[\./-]\d{1,4}$/)) {
		parts = text.split(/[\./-]/);
		j = this._parseInt(parts[0]);
		m = this._parseInt(parts[1]) - 1;
		a = this._parseInt(parts[2]);
		a = this._convertYear(a);
		return this._composeDate(a, m, j);
	}

	alert("Format de date inconnu : " + text);
	this._field.focus();
}

DateComponent.prototype._parseInt = function(str) {
	return parseInt(str.replace(/^0*/, ""));
}

DateComponent.prototype._convertYear = function(a) {
	if (a < 100) {
		if (a < 30) {
			a += 2000;
		} else {
			a += 1900;
		}
	}
	return a;
}

DateComponent.prototype._composeDate = function(a,m,j) {
	if (m > 11) m = 11;
	// if (j > _dayMax(m,a)) j = _dayMax(a,m) - 1;
	return new Date(a, m, j);
}

_dayMax = function(a,m) {
	return new Date(a, m, 0).getDate();
}

DateComponent.prototype.updateField = function() {
	if (!this.date) return;
	this._field.value = this.date.format();
}

DateComponent.prototype.updateCalendar = function() {
	ref_date = this.date ? this.date : new Date();
	this._mois.innerHTML = DateComponent.MONTH_NAMES[ref_date.getMonth()];
	this._annee.innerHTML = ref_date.getFullYear();
	// ------
	now = new Date();
	month = ref_date.debutMois();
	year = ref_date.debutAnnee();
	delta = (month.getDay() - 2); // empirique (je pensais -1 pour dim -> lun)
	if (delta < 0) delta += 7;
	for (sem = 0; sem < 6; sem++) {
		tr = this._table.firstChild.nextSibling.childNodes[sem];
		tr.childNodes[0].innerHTML = month.getWeek() + sem + 1;
		for (day = 0; day < 7; day++) {
			td = tr.childNodes[day+1];
			
			dayOfMonth = sem * 7 - delta + day;
			date = new Date(month);
			date.setDate(dayOfMonth);
			
			td.embeddedDate = date;
			td.innerHTML = date.getDate();
			td.style.color = date.sameMonth(month) ? "" : "#999";
			if (date.sameDate(now))
				td.innerHTML = "<strong>" + td.innerHTML + "</strong>";
			td.style.border = date.sameDate(this.date) ? "1px dashed #000" : "";
			td.style.padding = date.sameDate(this.date) ? "0px" : "1px";
		}
	}
}

DateComponent.prototype.prevMonth = function() {
	d = this.requireDate();
	d = new Date(d.getFullYear(), d.getMonth()-1, d.getDate());
	this.setDate(d);
}

DateComponent.prototype.nextMonth = function() {
	d = this.requireDate();
	d = new Date(d.getFullYear(), d.getMonth()+1, d.getDate());
	this.setDate(d);
}

DateComponent.prototype.prevYear = function() {
	d = this.requireDate();
	d = new Date(d.getFullYear()-1, d.getMonth(), d.getDate());
	this.setDate(d);
}

DateComponent.prototype.nextYear = function() {
	d = this.requireDate();
	d = new Date(d.getFullYear()+1, d.getMonth(), d.getDate());
	this.setDate(d);
}


/* ****************************************************************************
** Extensions à la classe Date
*/

Date.prototype.getWeek = function() {
	startOfYear = this.debutAnnee();
	return Math.floor((this.getTime() - startOfYear.getTime())/(1000*3600*24*7));
}

Date.prototype.sameMonth = function(other) {
	return other && this.getYear() == other.getYear() && this.getMonth() == other.getMonth();
}

Date.prototype.sameDate = function(other) {
	return this.sameMonth(other) && this.getDate() == other.getDate();
}

Date.prototype.sameDay = function(other) {
	return other && this.getDay() == other.getDay();
}

Date.prototype.format = function() {
	day = padWithZero(this.getDate(), 2);
	month = padWithZero(this.getMonth()+1, 2);
	year = this.getFullYear();
	return "" + day + "/" + month + "/" + year;
}

Date.prototype.debutMois = function() {
	return new Date(this.getFullYear(), this.getMonth(), 1);
}

Date.prototype.debutAnnee = function() {
	return new Date(this.getFullYear(), 0 /* janvier */, 1);
}
