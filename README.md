Neal Guarddin\
2406348282

# Link Website:
[Website Heroku Eshop Neal](https://boiling-cliffs-22997-639f62fcff8d.herokuapp.com/product/list)

# Refleksi 1 (Coding Standards):
Refleksi proses coding selama exercise 1:
1. Sempat ketika run application tidak ditemukan file htmlnya (CreateProduct.html dan ProductList.html), ternyata cuman salah format nama hehe
2. Implementasi clean code yang maksudnya mudah dibaca, kuat untuk dipahami, dan mudah dijaga. Konvensi nama variabel, 
nama fungsi, error handler, dan class organizations. Menurut prinsip Clean Code, fungsi bisa jadi pendek, bagus dinamai, dan rapih.
3. Mengurangi penggunaan comment code, sehingga setiap konvensi nama variabel dan method harus jelas
4. Untuk pembuatan fitur edit dan delete product menggunakan `productId` bertipe String. Awalnya mengerjakan untuk branch 
edit-product baru setelah itu delete-product. Untuk edit-product menggunakan method `updateProduct()`. Sementara delete-product
menggunakan methid `deleteProduct()`. Untuk halaman edit dibuat file html baru yaiut `EditProduct.html`
5. Terjadi kesalahan dalam git stash dan cara mergenya. Akhirnya belajar gimana cara pakai `git stash` dan `git stash pop`
dengan benar dan kapan penggunaannya ketika pindah branch. Dan apabila terjadi kesalahan dalam `git stash`, gimana cara retrieve
data berdasarkan `git stash list`

# Refleksi 2 (Coding Standards):
Refleksi proses coding selama exercise 2:
1. Setelah menulis unit test rasanya lebih kalem karena perubahan kecil di kode lebih gampang kedeteksi.
Namun, dalam penulisan unit test juga harus teliti. Harus jelas input, proses, dan output yang diharapkan.\
**Berapa banyak unit test dalam satu class?**\
Tidak ada angka baku. Tapi yang lebih penting:
   - Tiap method dites keadaan positif dan negatif
   - Tiap method/tes fokus pada satu skenario
   - Test harus mudah dibaca dan tidak saling bergantung

    **Bagaimana memastikan unit test “cukup”?**\
    Metrik paling membantu adalah *code coverage*(statement/branch coverage). Coverage membantu melihat bagian kode mana
   yang belum tersentuh test. Tapi coverage bukan tujuan utama. **Tujuan utama** adalah memverifikasi perilaku yang penting dan rawan error.

    **Kalau coverage 100%, apakah berarti tidak ada bug?**  
**Tidak**. 100% coverage tidak menjamin bebas bug, karena:
   - Test bisa “melewati” baris kode tanpa assertion yang kuat.
   - Edge case atau kombinasi input tertentu mungkin belum dites.
   - Bug bisa muncul di integrasi antar komponen, konfigurasi, atau concurrency.
   - Requirement bisa salah dipahami. Test jadi memverifikasi hal yang keliru.

2. Refleksi kebersihan kode saat membuat functional test baru\
   Jika saya diminta membuat functional test suite baru untuk memverifikasi
   jumlah item di product list, lalu saya menyalin setup yang sama seperti suite
   sebelumnya, maka kode **berpotensi kurang bersih karena duplikasi**.
**Potensi isu Clean Code:**
   - **Duplikasi (melanggar DRY):**
     setup `baseUrl`, `WebDriverWait`, dan navigasi halaman diulang.
   - **Magic strings & selector tersebar:**
     path seperti `/product/create` dan locator seperti `By.id("nameInput")`
     muncul di banyak tempat (redundan), sehingga rapuh saat UI berubah.
   - **Tight coupling ke detail UI:**
     perubahan kecil di HTML dapat mematahkan banyak test.
   - **Flaky test risk:**
     functional test bergantung timing/redirect; wait yang tidak konsisten
     bisa membuat test tidak stabil.
   - **Readability menurun:**
     boilerplate Selenium membuat intent test kurang terlihat.

    **Saran perbaikan:**
   - Ekstrak common setup ke helper atau base class (mis. `BaseFunctionalTest`).
   - Terapkan **Page Object Model (POM)**:
     buat `CreateProductPage` dan `ProductListPage` yang menyimpan locator dan aksi.
   - Gunakan konstanta untuk URL dan locator agar tidak tersebar (konsisten di semua pemakaian).
   - Buat helper reusable untuk membuat product (seed),
     agar test lain fokus pada assertion.
   - Jaga isolasi antar test:
     pastikan state data tidak bocor antar test (reset data bila perlu).

    Berdasarkan cara ini, harapannya test suite baru tetap ringkas, mudah dirawat,
dan lebih tahan terhadap perubahan UI.

# Refleksi 3 (CI/CD & Code Quality)

## 1) Code quality issue yang saya perbaiki + strategi saya
Selama exercise ini saya fokus beresin temuan dari static analysis (PMD) yang sifatnya kecil tapi beriis, supaya pipeline bersih dan kode lebih rapi.

Beberapa isu yang saya perbaiki:
- **Modifier `public` yang tidak perlu di interface** (Java interface method sudah otomatis `public`).  
  Saya hapus modifier yang redundant supaya nggak memunculkan warning dan biar konsisten dengan konvensi Java. (Bisa dilihat [src/main/java/id/ac/ui/cs/advprog/eshop/service/ProductService.java](src/main/java/id/ac/ui/cs/advprog/eshop/service/ProductService.java))
- **Import yang tidak dipakai / wildcard import** di controller.  
  Saya rapikan import dan buang yang tidak kepakai karena bikin warning dan bikin file terlihat “berantakan”. (Bisa dilihat [src/main/java/id/ac/ui/cs/advprog/eshop/controller/ProductController.java](src/main/java/id/ac/ui/cs/advprog/eshop/controller/ProductController.java))
- **Parameter yang tidak dipakai** (contoh: ada parameter method yang sebenarnya tidak digunakan).  
  Saya hapus supaya signature lebih bersih dan tidak misleading.

Strategi saya waktu fixing:
1) Lihat list warning/annotation dari workflow quality gate dulu.  
2) Milih yang paling aman (tanpa mengubah behaviour) untuk dikerjakan lebih dulu.  
3) Setiap perubahan saya commit kecil-kecil, lalu push untuk mastiin github workflow tetap jalan dan warning-nya benar-benar hilang.

## 2) Apakah workflow saya sudah memenuhi CI dan CD?
Menurut saya, workflow ini sudah memenuhi **Continuous Integration** karena setiap ada push/PR, GitHub Actions menjalankan proses otomatis untuk cek kualitas kode lewat workflow [PMD](.github/workflows/pmd.yml). Dengan begitu, masalah bisa ketahuan lebih cepat sebelum perubahan masuk ke branch utama.

Untuk **Continuous Deployment**, saya pakai **Heroku GitHub Automatic Deploy** (buildpack), jadi setiap ada perubahan yang masuk ke branch `main/master` di GitHub, Heroku akan otomatis build dan release versi terbaru. Karena deploy-nya via buildpack (bukan container), aplikasi dijalankan memakai command dari [Procfile](Procfile), dan versi Java dipin lewat [system.properties](system.properties).

Catatan: workflow [Deploy to Heroku (Docker)](.github/workflows/deploy-heroku.yml) saya nonaktifkan (manual saja) karena deployment-nya sudah ditangani langsung oleh Heroku GitHub integration, jadi tidak dobel jalur deploy.
