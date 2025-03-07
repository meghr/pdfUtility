document.addEventListener('DOMContentLoaded', function() {
    // Image Compression
    document.getElementById('compressionForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const formData = new FormData();
        const file = this.querySelector('input[type="file"]').files[0];
        const quality = this.querySelector('input[type="number"]').value;
        
        formData.append('file', file);
        formData.append('quality', quality);

        try {
            const response = await fetch('/api/compress', {
                method: 'POST',
                body: formData
            });
            
            if (response.ok) {
                const blob = await response.blob();
                downloadFile(blob, `compressed_${file.name}`);
            }
        } catch (error) {
            alert('Error compressing image: ' + error);
        }
    });

    // PDF Merger
    document.getElementById('mergerForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const formData = new FormData();
        const files = this.querySelector('input[type="file"]').files;
        const progressBar = this.querySelector('.progress');
        
        if (files.length < 2) {
            alert('Please select at least 2 PDF files to merge');
            return;
        }
    
        // Show progress bar
        progressBar.style.display = 'block';
        
        try {
            for (let file of files) {
                formData.append('files', file);
            }
    
            const response = await fetch('/api/merge-pdf', {
                method: 'POST',
                body: formData
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const blob = await response.blob();
            if (blob.size === 0) {
                throw new Error('Received empty response from server');
            }
            
            downloadFile(blob, 'merged.pdf');
        } catch (error) {
            console.error('Error merging PDFs:', error);
            alert('Error merging PDFs: ' + error.message);
        } finally {
            // Hide progress bar
            progressBar.style.display = 'none';
        }
    });
    // PDF Unlocker
    document.getElementById('unlockForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const formData = new FormData();
        const file = this.querySelector('input[type="file"]').files[0];
        const password = this.querySelector('input[type="password"]').value;
        
        formData.append('file', file);
        formData.append('password', password);

        try {
            const response = await fetch('/api/unlock-pdf', {
                method: 'POST',
                body: formData
            });
            
            if (response.ok) {
                const blob = await response.blob();
                downloadFile(blob, `unlocked_${file.name}`);
            }
        } catch (error) {
            alert('Error unlocking PDF: ' + error);
        }
    });

    // Image to PDF
    document.getElementById('imageToPdfForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const formData = new FormData();
        const file = this.querySelector('input[type="file"]').files[0];
        
        formData.append('file', file);

        try {
            const response = await fetch('/api/image-to-pdf', {
                method: 'POST',
                body: formData
            });
            
            if (response.ok) {
                const blob = await response.blob();
                downloadFile(blob, `${file.name}.pdf`);
            }
        } catch (error) {
            alert('Error converting image to PDF: ' + error);
        }
    });

    // Utility function to download files
    function downloadFile(blob, fileName) {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        a.remove();
    }
});