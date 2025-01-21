document.addEventListener("DOMContentLoaded",() => {
	document.getElementById("logoutLink").addEventListener("click", ()=>{
		document.getElementById("logoutForm").submit();
	})
	
Array.from(document.getElementsByClassName("changeUserStatusLink"))
	.forEach(link => link.addEventListener("click",() => {
		
		document.getElementById("changeUserId").setAttribute("value",link.getAttribute("data-id"));
		document.getElementById("changeUserStatus").setAttribute("value",link.getAttribute("data-status"));
		
		document.getElementById("changeUserName").innerText = link.getAttribute("data-user");
		document.getElementById("changeUserStatusBefore").innerText =  link.getAttribute("data-status") =="true" ? "Active": "Suspend";
		document.getElementById("changeUserStatusAfter").innerText = link.getAttribute("data-status") =="true"  ? "Suspend": "Active";
		
		const modal = new bootstrap.Modal("#userStatusChangeModal");
		modal.show();
	}))
	
	const pageSizeChageSelect = document.getElementById("pageSizeChageSelect");
	pageSizeChageSelect.addEventListener('change', () => {
		const formId = pageSizeChageSelect.getAttribute('data-form-id');
		const size = pageSizeChageSelect.value;
		const form = document.getElementById(formId);
		const sizeInput = document.createElement('input');
		sizeInput.setAttribute('type','hidden');
		sizeInput.setAttribute('name', 'size');
		sizeInput.setAttribute('value', size);
		form.appendChild(sizeInput);
		console.log("size input: " + sizeInput)
		form.submit();
	})
	
})