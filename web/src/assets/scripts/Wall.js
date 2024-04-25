import { MyGameObject } from "./MyGameObject";

export class Wall extends MyGameObject {
  constructor(r, c, gamemap, color) {
    super();

    this.r = r;
    this.c = c;
    this.gamemap = gamemap;
    this.color = color;
  }

  update() {
    this.render();
  }

  render() {
    const L = this.gamemap.L;
    const ctx = this.gamemap.ctx;

    ctx.fillStyle = this.color;
    ctx.fillRect(this.c * L, this.r * L, L, L);
  }
}
export class Frame extends Wall {
  constructor(r, c, gamemap) {
    super();

    this.r = r;
    this.c = c;
    this.gamemap = gamemap;
    this.color = "#0A6970"
    Wall.call(this.r, this.c, this.gamemap)
  }

  update() {
    //this.render();
  }

  render() {
    const L = this.gamemap.L;
    const ctx = this.gamemap.ctx;

    ctx.fillStyle = this.color;
    ctx.fillRect(this.c * L, this.r * L, L, L);
  }
}