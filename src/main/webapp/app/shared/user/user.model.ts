import {UserProfile} from '../user-profile/user-profile.model';

export const enum ContractType {
    'FULLTIME',
    'PARTTIME'
}

export class User {
    public id?: any;
    public login?: string;
    public firstName?: string;
    public lastName?: string;
    public email?: string;
    public activated?: Boolean;
    public langKey?: string;
    public authorities?: any[];
    public createdBy?: string;
    public createdDate?: Date;
    public lastModifiedBy?: string;
    public lastModifiedDate?: Date;
    public password?: string;
    public phone?: string;
    public dob?: any;
    public about?: string;
    public contractType?: ContractType;
    public businessUnitId?: any;
    public businessUnitName?: string;
    public userProfile?: UserProfile;
    constructor(
        id?: any,
        login?: string,
        firstName?: string,
        lastName?: string,
        email?: string,
        activated?: Boolean,
        langKey?: string,
        authorities?: any[],
        createdBy?: string,
        createdDate?: Date,
        lastModifiedBy?: string,
        lastModifiedDate?: Date,
        password?: string,
        phone?: string,
        dob?: any,
        about?: string,
        contractType?: ContractType,
        businessUnitId?: any,
        businessUnitName?: string,
        userProfile?: UserProfile,
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.firstName = firstName ? firstName : null;
        this.lastName = lastName ? lastName : null;
        this.email = email ? email : null;
        this.activated = activated ? activated : false;
        this.langKey = langKey ? langKey : null;
        this.authorities = authorities ? authorities : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.password = password ? password : null;
        this.dob = dob ? dob : null;
        this.phone = phone ? phone : null;
        this.about = about ? about : null;
        this.contractType = contractType ? contractType : null;
        this.businessUnitId = businessUnitId ? businessUnitId : null;
        this.businessUnitName = businessUnitName ? businessUnitName : null;
        this.userProfile = userProfile ? userProfile : new UserProfile;
    }
}
