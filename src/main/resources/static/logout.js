document.querySelector("#user-menu").addEventListener("click",function(e) {
	e.preventDefault();
	const menu = document.querySelector("#logout-menu");
	menu.style.display = (menu.style.display === "none" || menu.style.display === "") ? "block" : "none";
});
