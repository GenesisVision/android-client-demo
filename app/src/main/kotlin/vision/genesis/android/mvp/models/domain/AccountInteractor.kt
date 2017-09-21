package vision.genesis.android.mvp.models.domain

import vision.genesis.android.mvp.models.data.AccountInfo


class AccountInteractor {
    fun execute(): AccountInfo {
        //TODO replace test info
        val name = "Account 1"
        val avatar = "http://arsich.ru/traders_api/avatars/7.png"
        val key = "48329482sdfsdf..fsdfs123"
        return AccountInfo(name, avatar, key, 15, 1.18f)
    }
}