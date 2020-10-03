export default class Email {
    constructor() {
        this.to = [{}]
        this.bcc = []
        this.text = '';
        this.attachments = [];
        this.type = '';
        this.subject = '';
    }
}