GJCTHelper = {}
GJCTHelper.load = function(filename) {
try{load(filename)} 
	catch (e) {
		load(filename.replace(/\\/g,"/"))
	}
}
WScript = {}
WScript.Echo = print
WScript.LoadScriptFile = GJCTHelper.load
WScript.LoadModuleFile = GJCTHelper.load
WScript.Arguments = new Array(0,0,0,0,0);
