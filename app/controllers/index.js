/**
 * @author Alberto
 */

function androidMessage(text) {
	Titanium.UI.createNotification({
		duration : Ti.UI.NOTIFICATION_DURATION_SHORT,
		message : text
	}).show();
};

function iosMessage(text) {
	// Janela da mensagem.
	var indWin = Titanium.UI.createWindow();
	// View da mensagem.
	var indView = Titanium.UI.createView({
		height : Ti.UI.SIZE,
		width : Ti.UI.SIZE,
		borderRadius : 10,
		opacity : 0.7
	});
	// Label da mensagem.
	var message = Titanium.UI.createLabel({
		text : text
	});
	// Junta o label na view e a view na janela e abre a mesma..
	indWin.add(indView);
	indView.add(message);
	indWin.open();
	// Cria um timer para fechar a mensagem.
	setTimeout(function() {
		indWin.close({
			opacity : 0,
			duration : Ti.UI.NOTIFICATION_DURATION_SHORT
		});
	}, interval);
}

function webMessage(text) {
	alert(text);
}

/**
 * Notificação dos resultados.
 * @param {String} text Texto que será mostrado no aviso.
 */
function showMessage(text) {
	if (OS_ANDROID)
		androidMessage(text);
	else if (OS_IOS)
		iosMessage(text);
	else if (OS_MOBILEWEB)
		webMessage(text);
}

/**
 * Cria o cliente que fará o login.
 */
function createLoginPost() {
	return Titanium.Network.createHTTPClient({
		onload : function(event) {
			$.activityIndicator.hide();
			parseResult(this.responseText);
			toggleButtons(true);
		},
		onerror : function(event) {
			$.activityIndicator.hide();
			showMessage("Probelmas, verifique sua conexão com a rede PORTAL.");
			toggleButtons(true);
		},
		timeout : requestTimeout
	});
}

/**
 * Cria o cliente que fará o logout.
 */
function createLogoutPost() {
	return Titanium.Network.createHTTPClient({
		onload : function(event) {
			$.activityIndicator.hide();
			showMessage("Pedido de conexão enviado com sucesso.");
			toggleButtons(true);
		},
		onerror : function(event) {
			$.activityIndicator.hide();
			showMessage("Probelmas, verifique sua conexão com a rede PORTAL.");
			toggleButtons(true);
		},
		timeout : requestTimeout
	});
}

/**
 * Função para alterar o estado dos botões da interface.
 */
function toggleButtons(enable) {
	$.btLogin.enabled = enable;
	$.btLogout.enabled = enable;
}

/**
 * Método para autenticar na rede quando solicitado.
 */
function login() {
	toggleButtons(false);
	var params = {
		username : $.tfRegistry.value,
		password : $.tfPassword.value,
		buttonClicked : 4,
		err_flag : 0
	};
	$.activityIndicator.show();
	var loginPost = createLoginPost();
	loginPost.open("POST", "https://wifiportal.pucrs.br/login.html");
	loginPost.send(params);
}

/**
 * Método para sair da rede quando solicitado.
 */
function logout() {
	toggleButtons(false);
	var params = {
		userStatus : 1
	};
	$.activityIndicator.show();
	var logoutPost = createLogoutPost();
	logoutPost.open("POST", "https://wifiportal.pucrs.br/login.html");
	logoutPost.send(params);
}

/**
 * Verifica a resposta do servidor e informa o usuário.
 * @param {Object} result Texto que retornou do post.
 */
function parseResult(result) {
	if (result.indexOf("<title>Logged In</title>") != -1)
		showMessage("Autenticado com sucesso.");
	else {
		if (result.indexOf("The username specified cannot be used at this time.") != -1)
			showMessage("A matrícula já está sendo usada em outro dispositivo!");
		else if (result.indexOf("You are already logged in.") != -1)
			showMessage("Você já está autenticado.");
		else
			showMessage("Dados errados ou problemas na rede.");
	}
	// Salva a matrícula e a senha do usuário.
	Ti.App.Properties.setString("password", $.tfPassword.value);
	Ti.App.Properties.setString("registry", $.tfRegistry.value);
}

/**
 * Se já tiver dados salvos, carrega nos campos.
 */
if (Ti.App.Properties.getString("registry")) {
	$.tfPassword.value = Ti.App.Properties.getString("password");
	$.tfRegistry.value = Ti.App.Properties.getString("registry");
}
/**
 * Inicia o App.
 */
$.index.open();
