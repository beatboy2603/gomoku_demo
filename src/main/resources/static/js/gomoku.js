let isX = true;
let xList = [];
let oList = [];

genBoard = () => {
    let html = "<table id='gomokuTable'>";
    for (var i = 0; i < 10; i++) {
        html += "<tr>";
        for (var k = 0; k < 10; k++) {
            html += "<td onclick='markSpot(event)' data-posX='" + k + "' data-posY='" + i + "'></td>";
        }
        html += "</tr>";
    }
    html += "</table>";
    $("#gomokuBoard").html(html);
};

markSpot = (e) => {
    if (!$(e.target).html()) {
        let posX = $(e.target).attr("data-posX");
        let posY = $(e.target).attr("data-posY");

        if (isX) {
            $(e.target).html("<span>X</span>");
            xList.push(posX + " " + posY);
            isX = false;
        } else {
            $(e.target).html("<span>O</span>");
            oList.push(posX + " " + posY);
            isX = true;
        }
        checkWin(posX, posY);
    }
}

checkWin = (posX, posY) => {
    posX = parseInt(posX, 10);
    posY = parseInt(posY, 10);
    if (!isX) {
        if (checkHorizontal(posX, posY, xList) ||
                checkVertical(posX, posY, xList) ||
                checkPositiveDiagonal(posX, posY, xList) ||
                checkNegativeDiagonal(posX, posY, xList)) {
            console.log("x wins");
        }
    } else {
        if (checkHorizontal(posX, posY, oList) ||
                checkVertical(posX, posY, oList) ||
                checkPositiveDiagonal(posX, posY, oList) ||
                checkNegativeDiagonal(posX, posY, oList)) {
            console.log("o wins");
        }
    }
}

checkHorizontal = (posX, posY, cellList) => {

    let tempX = posX;
    let tempY = posY;
    let count = 1;
    for (var i = 0; i < 4; i++) {
        cellList.map(el => {
            if (el.localeCompare((tempX + 1) + " " + tempY) == 0) {
                count++;
            }
        });
        tempX++;
    }
    tempX = posX;
    tempY = posY;
    for (var i = 0; i < 4; i++) {
        cellList.map(el => {
            if (el.localeCompare((tempX - 1) + " " + tempY) == 0) {
                count++;
            }
        });
        tempX--;
    }
    if (count >= 5) {
        return true;
    } else {
        return false;
    }
}

checkVertical = (posX, posY, cellList) => {
    let tempX = posX;
    let tempY = posY;
    let count = 1;
    for (var i = 0; i < 4; i++) {
        cellList.map(el => {
            if (el.localeCompare(tempX + " " + (tempY + 1)) == 0) {
                count++;
            }
        });
        tempY++;
    }
    tempX = posX;
    tempY = posY;
    for (var i = 0; i < 4; i++) {
        cellList.map(el => {
            if (el.localeCompare(tempX + " " + (tempY - 1)) == 0) {
                count++;
            }
        });
        tempY--;
    }
    console.log(count);
    if (count >= 5) {
        return true;
    } else {
        return false;
    }
}

checkPositiveDiagonal = (posX, posY, cellList) => {
    let tempX = posX;
    let tempY = posY;
    let count = 1;
    for (var i = 0; i < 4; i++) {
        cellList.map(el => {
            if (el.localeCompare((tempX + 1) + " " + (tempY + 1)) == 0) {
                count++;

            }
        });
        tempX++;
        tempY++;
    }
    tempX = posX;
    tempY = posY;
    for (var i = 0; i < 4; i++) {
        cellList.map(el => {
            if (el.localeCompare((tempX - 1) + " " + (tempY - 1)) == 0) {
                count++;

            }
        });
        tempX--;
        tempY--;
    }
    if (count >= 5) {
        return true;
    } else {
        return false;
    }
}

checkNegativeDiagonal = (posX, posY, cellList) => {
    let tempX = posX;
    let tempY = posY;
    let count = 1;
    for (var i = 0; i < 4; i++) {
        cellList.map(el => {
            if (el.localeCompare((tempX - 1) + " " + (tempY + 1)) == 0) {
                count++;

            }
        });
        tempX--;
        tempY++;
    }
    tempX = posX;
    tempY = posY;
    for (var i = 0; i < 4; i++) {
        cellList.map(el => {
            if (el.localeCompare((tempX + 1) + " " + (tempY - 1)) == 0) {
                count++;

            }
        });
        tempX++;
        tempY--;
    }
    if (count >= 5) {
        return true;
    } else {
        return false;
    }
}