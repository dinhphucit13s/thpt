export class UserProfile {
    public id?: any;
    public workingLocation?: any;
    public jobTitle?: string;
    public userId?: any;
    public userLogin?: string;
    public timezoneId?: string
    constructor(
        id?: any,
        workingLocation?: any,
        jobTitle?: string,
        userId?: any,
        userLogin?: string,
        timezoneId?: string,
    ) {
        this.id = id ? id : null;
        this.workingLocation = workingLocation ? workingLocation : null;
        this.jobTitle = jobTitle ? jobTitle : null;
        this.userId = userId ? userId : null;
        this.userLogin = userLogin ? userLogin : null;
        this.timezoneId = timezoneId ? timezoneId : null;
    }
}
