
document.addEventListener("DOMContentLoaded", function() {
	
	const [one, two] = document.querySelectorAll('select');
	fetch('/loc')
		.then(r => r.json())
		.then(d => {
			let x = `<option value="" aria-required="true">------------- Select Location -------------</option>`;
			d.forEach(e => {
				x = x + ` <option value="${e.name}">${e.name}</option>`
			})
			one.innerHTML = x;
		})

	one.addEventListener('change', function() {
		fetch(`/areas/${one.value}`)
			.then(r => r.json())
			.then(d => {
				let x = `<option value="" aria-required="true" >--------------- Select Area ---------------</option>`;
				d.forEach(e => {
					x = x + ` <option value="${e.name}">${e.name}</option>`
				})
				two.innerHTML = x;
			})
	})

})
