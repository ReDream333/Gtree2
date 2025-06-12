// Получаем элементы бокового окна

const nodeViewMode = document.getElementById("nodeViewMode");
const nodeEditMode = document.getElementById("nodeEditMode");

const nodeInfoPanel = document.getElementById("nodeInfoPanel");
const nodeName = document.getElementById("nodeName");
const nodeBirthDate = document.getElementById("nodeBirthDate");
const nodeDeathDate = document.getElementById("nodeDeathDate");
const nodeComment = document.getElementById("nodeComment");
const nodeZodiac = document.getElementById("nodeZodiac");
const nodeViewPhoto = document.getElementById("nodeViewPhoto");
const deleteNodeButton = document.getElementById("deleteNodeButton");
const editNodeButton = document.getElementById("editNodeButton");
const biographyButton = document.getElementById("biographyButton");
const photoAlbumButton = document.getElementById("photoAlbumButton");
const closePanelButton = document.getElementById("closePanelButton");


const editLastName = document.getElementById("editLastName");
const editFirstName = document.getElementById("editFirstName");
const editBirthDate = document.getElementById("editBirthDate");
const editDeathDate = document.getElementById("editDeathDate");
const editComment  = document.getElementById("editComment");
const saveNodeButton = document.getElementById("saveNodeButton");
const cancelEditButton = document.getElementById("cancelEditButton");

let uploadedPhotoUrl = ""; // Переменная для хранения ссылки на фото

// Инициализация виджета Cloudinary
var myWidget = cloudinary.createUploadWidget({
    cloudName: 'gtree',
    uploadPreset: 'usersphoto'
}, (error, result) => {
    if (!error && result && result.event === "success") {
        console.log('Done! Here is the image info: ', result.info);

        // Сохраняем ссылку на загруженное изображение
        uploadedPhotoUrl = result.info.secure_url;

        // Обновляем превью фото в режиме редактирования
        nodeViewPhoto.src = uploadedPhotoUrl;
    }
});

// Привязываем кнопку для загрузки фото
document.getElementById("upload_widget").addEventListener("click", function() {
    myWidget.open();
}, false);

// Функция сохранения данных
function saveNodeData(nodeData) {
    const updatedData = {
        firstName: editFirstName.value,
        lastName: editLastName.value,
        birthDate: editBirthDate.value,
        deathDate: editDeathDate.value,
        comment:  editComment.value,
        photoUrl: uploadedPhotoUrl || nodeData.photo
    };

    // Отправляем данные на сервер - тут конечно должен быть ajax,
    // но я уже все мозги себе съела, потому что у меня не обновляется
    // даты в диаграмме без перезагрузки. Хоть убей, имя - да, даты - нет.
    // Плюс там какие-то скрытые камни появляются.
    // Пусть пока вот так тупо будет
    fetch(`/api/trees/${treeId}/nodes/${nodeData.key}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedData)
    })
        .then(response => response.json())
        .then(updatedNode => {
            myDiagram.model.startTransaction("updateNode");

            const nodeDatum = myDiagram.model.findNodeDataForKey(updatedNode.key);
            if (nodeDatum) {
                myDiagram.model.setDataProperty(nodeDatum, "fullName", updatedNode.fullName);
                myDiagram.model.setDataProperty(nodeDatum, "birthday", updatedNode.birthday || "Неизвестно");
                myDiagram.model.setDataProperty(nodeDatum, "death",    updatedNode.death    || "");
                myDiagram.model.setDataProperty(nodeDatum, "comment",  updatedNode.comment  || "");
                myDiagram.model.setDataProperty(nodeDatum, "photo",    updatedNode.photo    || "");
                myDiagram.model.setDataProperty(nodeDatum, "zodiacSign", updatedNode.zodiacSign || "");
            }

            myDiagram.model.commitTransaction("updateNode");


            nodeInfoPanel.style.display = "none";



        })
        .catch(error => {
            console.error("Ошибка обновления:", error);
            alert("Не удалось обновить ноду.");
        });
}

// Функция открытия панели с инфой о нодах
function openNodeInfoPanel(nodeData) {
    nodeInfoPanel.style.display = "block";

    // Устанавливаем фото
    if (typeof nodeData.photo !== "undefined" && nodeData.photo && nodeData.photo !== "null" && nodeData.photo.trim() !== "" ) {
        nodeViewPhoto.src = nodeData.photo;
    } else {
        nodeViewPhoto.src = "/images/ava.jpg"; // Дефолтное фото
    }

    nodeName.textContent = `${nodeData.fullName}`;
    nodeBirthDate.textContent = nodeData.birthday!== "null" ?  nodeData.birthday : "Неизвестно";
    nodeDeathDate.textContent = (nodeData.death !== "null" && nodeData.death != null && nodeData.death) ? nodeData.death : "Жив/Неизвестно о смерти";
    nodeComment.textContent = nodeData.comment || "";
    nodeZodiac.textContent = nodeData.zodiacSign ? nodeData.zodiacSign : "-";
    deleteNodeButton.style.display = nodeData.isLeaf ? "inline-block" : "none";

    nodeViewMode.style.display = "block";
    nodeEditMode.style.display = "none";

    editNodeButton.onclick = () => editNode(nodeData);
    deleteNodeButton.onclick = () => deleteNode(nodeData.key);
    biographyButton.onclick = () => window.location.href = `/biography?nodeId=${nodeData.key}`;
    photoAlbumButton.onclick = () => window.location.href = `/album?nodeId=${nodeData.key}`;
}

function editNode(nodeData) {
    // Заполняем редактируемые поля
    const [firstName, lastName] = nodeData.fullName.split(" ");
    editFirstName.value = firstName || "имя";
    editLastName.value = lastName || "фамилия";
    editBirthDate.value = nodeData.birthday || "";
    editDeathDate.value = nodeData.death || "";
    editComment.value  = nodeData.comment || "";

    if (nodeData.photo) {
        nodeViewPhoto.src = nodeData.photo;
    }

    // Переключаем режимы
    nodeViewMode.style.display = "none";
    nodeEditMode.style.display = "block";

    saveNodeButton.onclick = () => saveNodeData(nodeData);
    cancelEditButton.onclick = () => openNodeInfoPanel(nodeData);
}


function deleteNode(nodeKey) {
    if (!confirm("Вы действительно хотите удалить этого родственника?")) return;

    fetch(`/api/trees/${treeId}/nodes/${nodeKey}`, { method: "DELETE" })
        .then((response) => {
            if (!response.ok) throw new Error("Ошибка при удалении");
            // Удаляем связи из модели
            const linksToRemove = myDiagram.model.linkDataArray.filter(
                link => link.from === nodeKey || link.to === nodeKey
            );

            myDiagram.startTransaction("deleteNode");

            // Удаляем связи
            linksToRemove.forEach(link => myDiagram.model.removeLinkData(link));

            // Удаляем ноду
            myDiagram.model.removeNodeData(myDiagram.model.findNodeDataForKey(nodeKey));

            myDiagram.commitTransaction("deleteNode");

            nodeInfoPanel.style.display = "none";
        });
}

// Закрытие окна
closePanelButton.onclick = () => {
    nodeInfoPanel.style.display = "none";
};

// Слушатель кликов на ноды диаграммы
function setupNodeClickListener(myDiagram) {
    myDiagram.addDiagramListener("ObjectSingleClicked", function (e) {
        const part = e.subject.part;
        if (!(part instanceof go.Node)) return;

        const nodeData = part.data;
        const isLeaf = !myDiagram.model.linkDataArray.some((link) => link.from === nodeData.key);

        openNodeInfoPanel({ ...nodeData, isLeaf });
    });
}




// Слушатель для кнопки закрытия панели
closePanelButton.onclick = () => {
    nodeInfoPanel.style.display = "none";
};

// Привязка событий после инициализации диаграммы
//ваще не пон зачем тут эт обыло -----ок
