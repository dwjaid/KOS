import { MyGameObject } from "./MyGameObject";

export class Timer extends MyGameObject {
  constructor(maxTime) {
    super();

    this.time = Number(maxTime) * 1000;
  }

  update() {
    if (this.time > 0) {

      this.time = Number(this.time - this.timedelta);
    }
  }

}