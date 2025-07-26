function attemptSidLogin() {
    let sid = window.location.href.match(/[?&](dosid|sid)=([^&]+)/)
    let baseUrl = window.location.origin;
    if (sid == null || baseUrl == null) return; // No sid is on the url, return.

    if (typeof chrome !== 'undefined') sendMessage(chrome, sid[2], baseUrl)
    else if (typeof browser !== 'undefined') sendMessage(browser, sid[2], baseUrl)
}

function sendMessage(browser, sid, baseUrl) {
    browser.runtime.sendMessage({sid: sid, baseUrl: baseUrl}, () => {
        window.location.href = baseUrl + "/indexInternal.es?action=internalStart";
    })
}

attemptSidLogin();