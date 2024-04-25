const MY_GAME_OBJECTS = [];

export class MyGameObject {
  constructor() {
    MY_GAME_OBJECTS.push(this);
    this.timedelta = 0;
    this.has_start = false;
  }

  start() {

  }

  update() {

  }

  on_destroy() {

  }

  destroy() {
    this.on_destroy();

    for (let i in MY_GAME_OBJECTS) {
      const obj = MY_GAME_OBJECTS[i];
      if (obj == this) {
        MY_GAME_OBJECTS.splice(i);
        break;
      }
    }
    
  }
}

let last_timestamp;
const step = timestamp => {
  for (let obj of MY_GAME_OBJECTS) {
    if (!obj.has_start) {
      obj.has_start = true;
      obj.start();
    } else {
      obj.timedelta = timestamp - last_timestamp;
      obj.update();
    }
  }

  last_timestamp = timestamp;
  requestAnimationFrame(step)
}

requestAnimationFrame(step)