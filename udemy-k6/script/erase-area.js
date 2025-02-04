import {FormData} from 'https://jslib.k6.io/formdata/0.0.2/index.js';
import {getAuthValue, pollingSingleTaskRequests, sendRequest, updateIterations} from "./base.js";


const type = 'erase-area';
const requiredPolling = __ENV.K6_REQUIRED_POLLING === 'true';  // 환경변수에서 required_polling 값을 받아옴

export function eraseArea() {
    const uuid = sendRequest(type, () => {
        const formData = new FormData();
        formData.append('auth', `${getAuthValue()}`);
        formData.append('originalImageUrl', 'https://aicreation-file.miricanvas.com/images/erase-area/original/2024/10/13/16/68f00543-27bd-4167-8d14-7598c1c680df.png');
        formData.append('maskingImageUrl', 'https://aicreation-file.miricanvas.com/images/erase-area/masking/2024/10/13/16/0c2bcc79-c62e-4a41-bf89-ac5511d03852.png');

        return {
            body: formData.body(),
            headers: {'Content-Type': `multipart/form-data; boundary=${formData.boundary}`},
        };
    });

    if (requiredPolling && uuid) {
        pollingSingleTaskRequests(type, uuid);
    }

    updateIterations(type)
}