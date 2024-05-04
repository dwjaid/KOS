import { MyGameObject } from "./MyGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";
import { useStore } from 'vuex'

export class GameMap extends MyGameObject {
  constructor(ctx, parent, store) {
    super();

    this.ctx = ctx;
    this.parent = parent;
    this.store = store;
    this.gamemap = store.state.pk.gamemap;
    this.rows = this.gamemap.length;
    this.cols = this.gamemap[0].length;
    this.L = 0;
    this.walls = [];
    this.snakes = [
      new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
      new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
    ];
  

  }

  check_ready() {
    for (const snake of this.snakes) {
      if (snake.status !== "idle") return false;
      if (snake.direction === -1) return false;
    }
    return true;
  }


  create_walls() {
    for (let r = 0; r < this.rows; ++r) {
      for (let c = 0; c < this.cols; ++c) {
        if (this.gamemap[r][c]) {
          if (r == 0 || r == this.rows - 1 || c == 0 || c == this.cols - 1) this.walls.push(new Wall(r, c, this, "#0A6970"));
          else this.walls.push(new Wall(r, c, this, "#B37226"));
        }
      }
    }
  }

  add_listening_events() {
    this.ctx.canvas.focus();

    const [snake0, snake1] = this.snakes;
    this.ctx.canvas.addEventListener("keydown", e => {
      let d = -1, idx = -1;
      switch (e.key) {
        case 'w': idx = 0, d = 0; break;
        case 'd': idx = 0, d = 1; break;
        case 's': idx = 0, d = 2; break;
        case 'a': idx = 0, d = 3; break;
        case 'ArrowUp': idx = 1, d = 0; break;
        case 'ArrowRight': idx = 1, d = 1; break;
        case 'ArrowDown': idx = 1, d = 2; break;
        case 'ArrowLeft': idx = 1, d = 3; break;
        default: break;
      }
      
      if (d >= 0) {
        if (idx === 0) snake0.set_direction(d);
        else if (idx === 1) snake1.set_direction(d);
      }
    });
  }

  start() {
    this.create_walls();
    this.add_listening_events();
  }

  update_size() {
    this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }


  next_step() {
    for (const snake of this.snakes) {
      snake.next_step();
    }
  }

  check_valid(cell) {
    for (let wall of this.walls) {
      if (wall.r === cell.r && wall.c === cell.c) return false;
    }

    for (const snake of this.snakes) {
      let k = snake.cells.length;
      if (!snake.check_tail_increasing()) {
        k--;
      }
      for (let i = 0; i < k; ++i) {
        if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) return false;  
      }
    }
    return true;
  }

  update() {
    this.update_size();
    if (this.check_ready()) {
      this.next_step();
    }
    
    this.render();
  }

  render() {
    const color_even = "#AAD751", color_odd = "#A2D149";
    for (let r = 0; r < this.rows; ++r) {
      for (let c = 0; c < this.cols; ++c) {
        if ((r + c) % 2 == 0) {
          this.ctx.fillStyle = color_even;
        } else {
          this.ctx.fillStyle = color_odd;
        }
        this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
      }
    }
  }
}