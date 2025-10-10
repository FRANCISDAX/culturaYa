const hamburguesa = document.getElementById('hamburguesa');
  const menu = document.getElementById('menu');

  hamburguesa.addEventListener('click', () => {
    menu.classList.toggle('activo');
  });


function enviar() {
    
    const valEmpresa = /^[A-Za-z0-9\s\.\,&-]{3,}$/; 
    const valContacto = /^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]{3,}$/; 
    const valCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;  
    const valTelefono = /^\+?[0-9\s]{7,15}$/;     
    const valMensaje = /^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\s.,;:¡!¿?\-'()"–—\n]{5,500}$/m;

    let empresa = document.getElementById("empresa").value.trim();
    let contacto = document.getElementById("contacto").value.trim();
    let correo = document.getElementById("correo").value.trim();
    let telefono = document.getElementById("telefono").value.trim();
    let mensaje = document.getElementById("mensaje").value.trim();

   switch (true) {
      case !valEmpresa.test(empresa):
        alert("Ingrese un nombre de empresa válido (mínimo 3 caracteres).");
        break;

      case !valContacto.test(contacto):
        alert("Ingrese un nombre de contacto válido (solo letras y espacios).");
        break;

      case !valCorreo.test(correo):
        alert("Ingrese un correo electrónico válido.");
        break;

      case !valTelefono.test(telefono):
        alert("Ingrese un teléfono válido (ejemplo: +51 999 999 999).");
        break;

      case !valMensaje.test(mensaje):
        alert("El mensaje debe tener al menos 5 caracteres.");
        break;

      default:
        alert("✅ Mensaje enviado con éxito");
        document.getElementById("formulario").reset();
    }
  }

  function suscribete() {

        let input = document.getElementById("dato").value.trim();
        const valSuscribete = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; 
        
        if (valSuscribete.test(input)){
            alert("✅ ¡Suscripción exitosa!");
          } else {
            alert("❌ Ingrese un correo válido (ejemplo: usuario@dominio.com)");
      }
  }
