/*
 * @Author: Chirag Maheshwari
 * 
 * Contains function to manipulate the Url of
 * the browser ans easily change between states.
 */
var Url = {
	searchParameterValue: 
		function(parameter) {
			if(window.location.search.length == 0)
				return null;
			
			var searchUrl = window.location.search.substring(1);
			var indexParameter = searchUrl.search(parameter + '=');
			
			if(indexParameter != -1) {
				var value = searchUrl.substring(indexParameter + parameter.length + 1),
					index = value.indexOf('&');
				
				if(index == -1)
					return value;
				else {
					return value.substring(0, index);
				}
			} else
				return null;
		},
	
	pushStateSearch:
		function (parameter, value) {
			var currentValue = this.searchParameterValue(parameter);
			
			if(currentValue == null) {
				if(window.location.search.length > 0) {
					window.history.replaceState({}, "", 
							window.location.search + "&" + encodeURIComponent(parameter) + '=' + encodeURIComponent(value));
				}
				else
					window.history.replaceState({}, "", 
							"?" + encodeURIComponent(parameter) + '=' + encodeURIComponent(value));
			} else {
				if(currentValue == value)
					return;
				else {
					var newSearch = window.location.search.replace(
							parameter + '=' + currentValue,
							encodeURIComponent(parameter) + '=' + encodeURIComponent(value));

					window.history.replaceState({}, "", 
							newSearch);
				}
			}
		}
};