String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};

console.log("this script is running");
document.querySelector('#navbar-collapse2 .nav :nth-child(4) a').href = "../user/logout";
// $(document).ready(function () {
//
// });

