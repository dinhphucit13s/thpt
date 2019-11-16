export class Selects {
    constructor(
        public id?: number,
        public name?: string,
    ) {
    }
}

export class ReuseSelect {
    constructor(
        public name?: string,
        public select?: Selects[],
    ) {
    }
}
