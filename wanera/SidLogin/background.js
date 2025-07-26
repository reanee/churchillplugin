if (typeof chrome !== 'undefined') {
    chrome.runtime.onMessage.addListener(message => {
        chrome.cookies.set({name: "dosid", url: message.baseUrl, value: message.sid});
    })
} else if (typeof browser !== 'undefined') {
    browser.runtime.onMessage.addListener(message => {
        browser.cookies.set({name: "dosid", url: message.baseUrl, value: message.sid});
    })
}