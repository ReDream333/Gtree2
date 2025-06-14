// Получаем элементы бокового окна

const nodeViewMode = document.getElementById("nodeViewMode");
const nodeEditMode = document.getElementById("nodeEditMode");

const nodeInfoPanel = document.getElementById("nodeInfoPanel");
const nodeName = document.getElementById("nodeName");
const nodeBirthDate = document.getElementById("nodeBirthDate");
const nodeDeathDate = document.getElementById("nodeDeathDate");
const nodeComment = document.getElementById("nodeComment");
const nodeZodiac = document.getElementById("nodeZodiac");
const nodeZodiacIcon = document.getElementById("nodeZodiacIcon");
const nodeViewPhoto = document.getElementById("nodeViewPhoto");
const deleteNodeButton = document.getElementById("deleteNodeButton");
const editNodeButton = document.getElementById("editNodeButton");
const biographyButton = document.getElementById("biographyButton");
const photoAlbumButton = document.getElementById("photoAlbumButton");
const closePanelButton = document.getElementById("closePanelButton");
const compatButton = document.getElementById("compatButton");

const compatModal = document.getElementById("compatModal");
const compatModalClose = document.getElementById("compatModalClose");
const compatResultText = document.getElementById("compatResultText");

let firstCompatKey = null;
let secondCompatKey = null;


const editLastName = document.getElementById("editLastName");
const editFirstName = document.getElementById("editFirstName");
const editBirthDate = document.getElementById("editBirthDate");
const editDeathDate = document.getElementById("editDeathDate");
const editComment  = document.getElementById("editComment");
const saveNodeButton = document.getElementById("saveNodeButton");
const cancelEditButton = document.getElementById("cancelEditButton");

let uploadedPhotoUrl = ""; // Переменная для хранения ссылки на фото
let currentNodeId = null;

const zodiacIcons = {
    "Овен": "♈",
    "Телец": "♉",
    "Близнецы": "♊",
    "Рак": "♋",
    "Лев": "♌",
    "Дева": "♍",
    "Весы": "♎",
    "Скорпион": "♏",
    "Стрелец": "♐",
    "Козерог": "♑",
    "Водолей": "♒",
    "Рыбы": "♓"
};

function fetchCompatibility(id1, id2) {
    return fetch(`/api/trees/${treeId}/nodes/compatibility?first=${id1}&second=${id2}`)
        .then(r => r.json())
        .then(res => res.percent ?? null)
        .catch(() => null);
}

function showCompatResult(percent) {
    if (percent === null) {
        compatResultText.textContent = 'Невозможно совместить, попробуйте с другой нодой';
    } else {
        compatResultText.textContent = `Совместимость: ${percent}%`;
        if (percent >= 70) {
            confetti({particleCount:150, spread:120, emojis:['❤️']});
        } else if (percent >= 40) {
            confetti({particleCount:120, spread:100});
        } else {
            confetti({particleCount:80, spread:120, emojis:['💧','⚡']});
        }
    }
    compatModal.style.display = 'flex';
}

compatModalClose.onclick = () => {
    compatModal.style.display = 'none';
    if (firstCompatKey) {
        const firstData = myDiagram.model.findNodeDataForKey(firstCompatKey);
        const secondData = secondCompatKey ? myDiagram.model.findNodeDataForKey(secondCompatKey) : null;

        myDiagram.model.startTransaction('unhighlight');
        myDiagram.model.setDataProperty(firstData, 'highlighted', false);
        if (secondData) {
            myDiagram.model.setDataProperty(secondData, 'highlighted', false);
        }
        myDiagram.model.commitTransaction('unhighlight');

        firstCompatKey = null;
        secondCompatKey = null;
        compatButton.disabled = false;
    }
};



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
        if (currentNodeId) {
            fetch(`/api/trees/${treeId}/nodes/${currentNodeId}/saveNodePhoto`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ imageUrl: uploadedPhotoUrl })
            })
                .then(r => r.ok ? r.json() : Promise.reject())
                .then(updated => {
                    const nodeDatum = myDiagram.model.findNodeDataForKey(updated.key);
                    if (nodeDatum) {
                        myDiagram.model.startTransaction('updateNodePhoto');
                        myDiagram.model.setDataProperty(nodeDatum, 'photo', updated.photo);
                        myDiagram.model.commitTransaction('updateNodePhoto');
                    }
                })
                .catch(() => alert('Ошибка при обновлении фото ноды'));
        }
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
async function openNodeInfoPanel(nodeData) {
    nodeInfoPanel.style.display = "block";

    // Устанавливаем фото
    try {
        const res = await fetch(`/api/trees/${treeId}/nodes/${nodeData.key}`);
        if (res.ok) {
            const fresh = await res.json();
            nodeData.photo = fresh.photo;
        }
    } catch (e) {
        console.error('Не удалось обновить данные ноды', e);
    }

    if (nodeData.photo && nodeData.photo !== "null" && nodeData.photo.trim() !== "") {
        nodeViewPhoto.src = nodeData.photo;
    } else {
        nodeViewPhoto.src = "/images/ava.jpg"; // Дефолтное фото
    }

    nodeName.textContent = `${nodeData.fullName}`;
    nodeBirthDate.textContent = nodeData.birthday!== "null" ?  nodeData.birthday : "Неизвестно";
    nodeDeathDate.textContent = (nodeData.death !== "null" && nodeData.death != null && nodeData.death) ? nodeData.death : "Жив/Неизвестно о смерти";
    nodeComment.textContent = nodeData.comment || "";
    nodeZodiac.textContent = nodeData.zodiacSign ? nodeData.zodiacSign : "-";
    nodeZodiacIcon.textContent = nodeData.zodiacSign ? (zodiacIcons[nodeData.zodiacSign] || "") : "";
    deleteNodeButton.style.display = nodeData.isLeaf ? "inline-block" : "none";

    compatButton.disabled = false;

    nodeViewMode.style.display = "block";
    nodeEditMode.style.display = "none";

    editNodeButton.onclick = () => editNode(nodeData);
    deleteNodeButton.onclick = () => deleteNode(nodeData.key);
    biographyButton.onclick = () => window.location.href = `/biography?nodeId=${nodeData.key}`;
    photoAlbumButton.onclick = () => window.location.href = `/album?nodeId=${nodeData.key}`;
    compatButton.onclick = () => startCompatibility(nodeData.key);
}

function editNode(nodeData) {
    currentNodeId = nodeData.key;
    // Заполняем редактируемые поля
    const [firstName, lastName] = nodeData.fullName.split(" ");
    editFirstName.value = firstName || "имя";
    editLastName.value = lastName || "фамилия";
    editBirthDate.value = nodeData.birthday || "";
    editDeathDate.value = nodeData.death || "";
    editComment.value  = nodeData.comment || "";

    if (nodeData.photo) {
        nodeViewPhoto.src = nodeData.photo;
        uploadedPhotoUrl = nodeData.photo;
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
    currentNodeId = null;
};

function startCompatibility(nodeKey) {
    firstCompatKey = nodeKey;
    secondCompatKey = null;
    const data = myDiagram.model.findNodeDataForKey(nodeKey);
    if (data) {
        myDiagram.model.startTransaction("highlight");
        myDiagram.model.setDataProperty(data, "highlighted", true);
        myDiagram.model.commitTransaction("highlight");
    }
    compatButton.disabled = true;
}

// Слушатель кликов на ноды диаграммы
function setupNodeClickListener(myDiagram) {
    myDiagram.addDiagramListener("ObjectSingleClicked", function (e) {
        const part = e.subject.part;
        if (!(part instanceof go.Node)) return;

        const nodeData = part.data;
        if (firstCompatKey && nodeData.key !== firstCompatKey) {
            const firstData = myDiagram.model.findNodeDataForKey(firstCompatKey);
            secondCompatKey = nodeData.key;
            const secondData = myDiagram.model.findNodeDataForKey(secondCompatKey);

            myDiagram.model.startTransaction("highlight-second");
            myDiagram.model.setDataProperty(secondData, "highlighted", true);
            myDiagram.model.commitTransaction("highlight-second");

            fetchCompatibility(firstCompatKey, secondCompatKey)
                .then(showCompatResult);
        } else {
            const isLeaf = !myDiagram.model.linkDataArray.some((link) => link.from === nodeData.key);
            openNodeInfoPanel({ ...nodeData, isLeaf });
        }

        openNodeInfoPanel({ ...nodeData, isLeaf });
    });
}




// Слушатель для кнопки закрытия панели
closePanelButton.onclick = () => {
    nodeInfoPanel.style.display = "none";
    currentNodeId = null;
};

// Привязка событий после инициализации диаграммы
//ваще не пон зачем тут эт обыло -----ок
