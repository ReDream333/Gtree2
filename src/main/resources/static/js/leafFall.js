function getRandomLeafImage() {
    const leafImages = [
        "../images/yellow-leaf.png",
        "../images/orange-leaf.png",
        "../images/red-leaf.png"
    ];
    return leafImages[Math.floor(Math.random() * leafImages.length)];
}

particlesJS("particles-js", {
    particles: {
        number: {
            value: 30, // Количество листьев
            density: {
                enable: true,
                value_area: 200
            }
        },
        shape: {
            type: "image",
            image: {
                src: getRandomLeafImage(),
                width: 100,
                height: 100
            }
        },
        line_linked: {
            enable: false
        },
        size: {
            value: 25,
            random: true
        },
        move: {
            enable: true,
            speed: 2, // Скорость падения
            direction: "bottom", // Направление
            random: false,
            straight: false,
            out_mode: "out",
            bounce: false
        }
    },
    retina_detect: true
});