
// 获取设备信息
function getDeviceInfo() {
    const devicePixelRatio = window.devicePixelRatio;  // 设备像素比
    const userAgent = navigator.userAgent;  // 用户代理
    const language = navigator.language;  // 浏览器语言
    const maxTouchPoints = navigator.maxTouchPoints;  // 最大触摸点数（如果支持）
    const screenWidth = screen.width;
    const screenHeight = screen.height;
    const hardwareConcurrency = navigator.hardwareConcurrency;
    const deviceMemory = navigator.deviceMemory

    // 合并设备信息为一个字符串
    return `${screenWidth}|${screenHeight}|${devicePixelRatio}|${hardwareConcurrency}|${deviceMemory}|${userAgent}|${language}|${maxTouchPoints}`;
}

// 哈希编码，转成长度64的字符串（十六进制数的字符串）
function generateDeviceIdentifier() {
    let deviceInfo = getDeviceInfo()

    return crypto.subtle.digest("SHA-256", new TextEncoder().encode(deviceInfo))
        .then(hashBuffer => {
            let hashArray = Array.from(new Uint8Array(hashBuffer));
            return hashArray.map(byte => byte.toString(16).padStart(2, "0")).join("");  // 返回哈希值
        });
}


// 是异步的，所以要调then
generateDeviceIdentifier().then(userIdentifier => {
    // 取前32位
    console.log("生成的用户标识：", userIdentifier.substring(0,32));
});

