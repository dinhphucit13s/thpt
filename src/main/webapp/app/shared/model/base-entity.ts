export interface BaseEntity {
    // using type any to avoid methods complaining of invalid type
    id?: any;
    isActiveOnAdmin?: any;
    isActiveOnOP?: any;
}
