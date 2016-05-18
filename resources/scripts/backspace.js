document.onkeydown = function(e) {stopDefaultBackspaceBehaviour(e);}
document.onkeypress = function(e) {stopDefaultBackspaceBehaviour(e);}

function stopDefaultBackspaceBehaviour(event) {
  var event = event || window.event;
  if (event.keyCode == 8) {
    var elements = "HTML, BODY, TABLE, TBODY, TR, TD, DIV";
    var d = event.srcElement || event.target;
    var regex = new RegExp(d.tagName.toUpperCase());
    if (d.contentEditable != 'true') {
      if (regex.test(elements)) {
          event.preventDefault ? event.preventDefault() : event.returnValue = false;
      }
    }
  }
} 