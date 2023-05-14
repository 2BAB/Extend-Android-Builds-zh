package me.xx2bab.extendagp.library1

class LibraryApi {

    private var client: HttpClient? = null

    fun execute() {
        println("execute() is called from LibraryApi...")
        if (client == null) {
            setupClientInternally()
        }
        println(client?.post())
    }

    fun setClient(client: HttpClient) {
        this.client = client
    }

    private fun setupClientInternally() {
        this.client = Class.forName("me.xx2bab.extendagp.library1.OkHttpClient")
            .getConstructor().newInstance() as HttpClient
    }
}