document.addEventListener("DOMContentLoaded", function () {
    // Инициализация виджета Cloudinary
    var myWidget = cloudinary.createUploadWidget({
            cloudName: 'gtree',
            uploadPreset: 'usersphoto'
        }, (error, result) => {
            if (!error && result && result.event === "success") {
                console.log('Done! Here is the image info: ', result.info);
                const uploadedImageUrl = result.info.secure_url;
                fetch(contextPath + '/profile/saveProfilePhoto', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        imageUrl: uploadedImageUrl
                    })
                })
                    .then(response => response.json())
                    .then(data => {
                        console.log('Response from server:', data);
                        updateProfilePhoto(uploadedImageUrl);
                    })
                    .catch(err => {
                        console.error('Ошибка сохранения на сервере:', err);
                        alert('Ошибка при обновлении фото профиля.');
                    });
            }
        }
    );

    // Функция для обновления фото пользователя
    function updateProfilePhoto(newImageUrl) {
        const userPhoto = document.querySelector('.user-photo');
        if (userPhoto) {
            userPhoto.src = newImageUrl; // Обновляем атрибут src
        }
    }

    // Обработчик кнопки загрузки фото
    var uploadWidgetButton = document.getElementById("upload_widget");
    if (uploadWidgetButton) {
        uploadWidgetButton.addEventListener("click", function(){
            myWidget.open();
        }, false);
    }

    // Функция для подтверждения удаления аккаунта
    function confirmDelete() {
        return confirm('Вы уверены, что хотите удалить свой аккаунт? Это действие необратимо.');
    }

    // Обработчик клика по кнопке удаления аккаунта
    document.getElementById("delete-button").addEventListener("click", function() {
        if (confirmDelete()) {
            fetch(contextPath + 'delete', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.status === "success") {
                        window.location.href = contextPath + '/home.ftl';
                    } else {
                        alert("Не удалось удалить аккаунт: " + data.message);
                    }
                })
                .catch(error => {
                    console.error("Ошибка:", error);
                    alert("Произошла ошибка при удалении аккаунта.");
                });
        }
    });


});
