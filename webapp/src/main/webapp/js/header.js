
/*
 * Chrono
 */

var chrono = document.getElementById("chrono");
chrono.running = false;
chrono.start_time = 0;

chrono.start = function() {
	this.start_time = new Date().getTime();
	if (this.running) return;
	this.running = true;
	this.updateText();
	setTimeout("chrono.update()", 160);
}

chrono.stop = function() {
	this.running = false;
	this.updateText();
}

chrono.update = function() {
	if (this.running != true) return;
	this.updateText();
	setTimeout("chrono.update()", 160);
}

chrono.updateText = function () {
	var elapsed = new Date().getTime() - chrono.start_time;
	this.innerHTML = (elapsed * 1.0 / 1000).toFixed(2).replace('.',',') + "s";
}

chrono.start();